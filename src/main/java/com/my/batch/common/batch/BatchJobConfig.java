package com.my.batch.common.batch;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.domain.BatchStatus;
import com.my.batch.domain.Notification;
import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.BatchStatusRepository;
import com.my.batch.service.ExchangeService;
import com.my.batch.service.NotificationService;
import com.my.batch.service.WebScrapingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@AllArgsConstructor
public class BatchJobConfig {

    private final ExchangeUtils exchangeUtils;
    private final ExchangeService exchangeService;
    private final BatchStatusRepository batchStatusRepository;
    private final NotificationService notificationService;
    private final WebScrapingService webScrapingService;

    private final String SUCCESS_EXIT_STATUS = "COMPLETED";

    private final String EXCHANGE_JOB_NAME = "exchangeSaveJob";
    private final String EXCHANGE_LAST_STEP_NAME = "exchangeSaveStep";

    private final String NOTIFICATION_JOB_NAME = "notificationJob";
    private final String NOTIFICATION_LAST_STEP_NAME = "sendMsgStep";

    private final String SCRAP_SAVE_JOB_NAME = "scrapAndSaveJob";
    private final String SCRAP_SAVE_LAST_STEP_NAME = "saveStep";

    @Bean
    public Job notificationJob(JobRepository jobRepository, Step validPeriodStep, Step sendMsgStep) {
        return new JobBuilder(NOTIFICATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(validPeriodStep)
                .on(SUCCESS_EXIT_STATUS)
                .to(sendMsgStep)
                .on(SUCCESS_EXIT_STATUS)
                .end()
                .end()
                .build();
    }

    @Bean
    public Job exchangeSaveJob(JobRepository jobRepository, Step parsingStep, Step exchangeSaveStep) {
        return new JobBuilder(EXCHANGE_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(parsingStep)
                .on(SUCCESS_EXIT_STATUS)
                .to(exchangeSaveStep)
                .on(SUCCESS_EXIT_STATUS)
                .end()
                .end()
                .build();
    }

    @Bean
    public Job scrapAndSaveJob(JobRepository jobRepository, Step scrapStep, Step saveStep) {
        return new JobBuilder(SCRAP_SAVE_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(scrapStep)
                .on(SUCCESS_EXIT_STATUS)
                .to(saveStep)
                .on(SUCCESS_EXIT_STATUS)
                .end()
                .end()
                .build();
    }

    @Bean
    public Step parsingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener exchangeSaveStepExecutionListener) {
        return new StepBuilder("parsingStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeWebApiResponseDto> result = exchangeUtils.getExchangeDataAsDtoList(null);

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("result", result);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(exchangeSaveStepExecutionListener)
                .build();
    }

    @Bean
    public Step exchangeSaveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener exchangeSaveStepExecutionListener) {
        return new StepBuilder("exchangeSaveStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeWebApiResponseDto> result = (List<ExchangeWebApiResponseDto>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("result");
                    exchangeService.saveExchangeList(result);

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(exchangeSaveStepExecutionListener)
                .build();
    }

    @Bean
    public Step validPeriodStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener notificationStepExecutionListener) {
        return new StepBuilder("validPeriodStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<Notification> result = notificationService.validPeriod();

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("result", result);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(notificationStepExecutionListener)
                .build();
    }

    @Bean
    public Step sendMsgStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener notificationStepExecutionListener) {
        return new StepBuilder("sendMsgStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<Notification> result = (List<Notification>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("result");
//                    notificationService.sendNotificationMsg(result);
                    notificationService.sendNotificationMsgToKafka(result); // > consumer listener가 듣고 있음

                    contribution.setExitStatus(ExitStatus.COMPLETED);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(notificationStepExecutionListener)
                .build();
    }

    @Bean
    public Step scrapStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener scrapAndSaveStepExecutionListener) {
        return new StepBuilder("scrapStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeScrapResponseDto> result = webScrapingService.extractInformation();

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("result", result);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(scrapAndSaveStepExecutionListener)
                .build();
    }

    @Bean
    public Step saveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener scrapAndSaveStepExecutionListener) {
        return new StepBuilder("saveStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeScrapResponseDto> result = (List<ExchangeScrapResponseDto>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("result");
                    exchangeService.saveScrapExchangeList(result);

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(scrapAndSaveStepExecutionListener)
                .build();
    }

    @StepScope
    @Bean
    public StepExecutionListener exchangeSaveStepExecutionListener() {
        return getStepExecutionListener(EXCHANGE_JOB_NAME, EXCHANGE_LAST_STEP_NAME);
    }

    @StepScope
    @Bean
    public StepExecutionListener notificationStepExecutionListener() {
        return getStepExecutionListener(NOTIFICATION_JOB_NAME, NOTIFICATION_LAST_STEP_NAME);
    }

    @StepScope
    @Bean
    public StepExecutionListener scrapAndSaveStepExecutionListener() {
        return getStepExecutionListener(SCRAP_SAVE_JOB_NAME, SCRAP_SAVE_LAST_STEP_NAME);
    }

    @NotNull
    private StepExecutionListener getStepExecutionListener(String jobName, String lastJobName) {
        return new StepExecutionListener() {

            LocalDateTime startTime = null;

            @Override
            public void beforeStep(StepExecution stepExecution) {
                startTime = stepExecution.getStartTime();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {

                BatchStatus batchStatus = BatchStatus.builder()
                        .workflowId(String.valueOf(stepExecution.getJobExecutionId()))
                        .workflowName(jobName)
                        .status(stepExecution.getExitStatus().getExitCode())
                        .startTime(startTime)
                        .build();
                // 마지막 스텝이 COMPLETED 인 상태로 종료
                if (stepExecution.getStatus().name().equals(SUCCESS_EXIT_STATUS) && stepExecution.getStepName().equals(lastJobName)) {
                    LocalDateTime endDate = LocalDateTime.now();

                    batchStatus.setEndTime(endDate);
                    batchStatus.setDuration(Duration.between(startTime, endDate).toMillis());
                    batchStatusRepository.save(batchStatus);
                }

                // 스텝이 COMPLETED 가 아닌 상태로 종료
                if (!stepExecution.getStatus().name().equals(SUCCESS_EXIT_STATUS)) {
                    LocalDateTime endDate = LocalDateTime.now();

                    batchStatus.setEndTime(LocalDateTime.now());
                    batchStatus.setFailedStepName(stepExecution.getStepName());
                    batchStatus.setErrMsg(stepExecution.getExitStatus().getExitDescription());
                    batchStatus.setDuration(Duration.between(startTime, endDate).toMillis());
                    batchStatusRepository.save(batchStatus);
                }

                return stepExecution.getExitStatus();
            }
        };
    }
}
