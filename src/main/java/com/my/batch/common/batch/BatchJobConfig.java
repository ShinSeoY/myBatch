package com.my.batch.common.batch;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.domain.BatchStatus;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.BatchStatusRepository;
import com.my.batch.service.ExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@AllArgsConstructor
public class BatchJobConfig {

    private final ExchangeUtils exchangeUtils;
    private final ExchangeService exchangeService;
    private final BatchStatusRepository batchStatusRepository;
    private final String JOB_NAME = "exchangeSaveJob";
    private final String LAST_STEP_NAME = "saveStep";
    private final String SUCCESS_EXIT_STATUS = "COMPLETED";

    @Bean
    public Job exchangeSaveJob(JobRepository jobRepository, Step parsingStep, Step saveStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(parsingStep)
                .on(SUCCESS_EXIT_STATUS)
                .to(saveStep)
                .on(SUCCESS_EXIT_STATUS)
                .end()
                .end()
                .build();
    }

    @Bean
    public Step parsingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener stepExecutionListener) {
        return new StepBuilder("parsingStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeWebApiResponseDto> result = exchangeUtils.getExchangeDataAsDtoList(null);

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("result", result);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step saveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StepExecutionListener stepExecutionListener) {
        return new StepBuilder("saveStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<ExchangeWebApiResponseDto> result = (List<ExchangeWebApiResponseDto>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("result");
                    exchangeService.saveExchangeList(result);

                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .build();
    }

    @StepScope
    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {

            LocalDateTime startTime = null;

            @Override
            public void beforeStep(StepExecution stepExecution) {
                startTime = stepExecution.getStartTime();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {

                BatchStatus batchStatus = BatchStatus.builder()
                        .jobName(JOB_NAME)
                        .status(stepExecution.getExitStatus().getExitCode())
                        .startTime(startTime)
                        .failStepName("-")
                        .errMsg("-")
                        .build();

                // 마지막 스텝이 COMPLETED 인 상태로 종료
                if (stepExecution.getStatus().name().equals(SUCCESS_EXIT_STATUS) && stepExecution.getStepName().equals(LAST_STEP_NAME)) {
                    batchStatus.setEndTime(LocalDateTime.now());
                    batchStatusRepository.save(batchStatus);
                }

                // 스텝이 COMPLETED 가 아닌 상태로 종료
                if (!stepExecution.getStatus().name().equals(SUCCESS_EXIT_STATUS)) {
                    batchStatus.setEndTime(LocalDateTime.now());
                    batchStatus.setFailStepName(stepExecution.getStepName());
                    batchStatus.setErrMsg(stepExecution.getExitStatus().getExitDescription());
                    batchStatusRepository.save(batchStatus);
                }

                return stepExecution.getExitStatus();
            }
        };
    }
}
