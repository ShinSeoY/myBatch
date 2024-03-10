package com.my.batch.common.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class BatchJobConfig {
    @Bean
    public Job exchangeSaveJob(JobRepository jobRepository, Step parsingStep, Step saveStep, Step successStep, Step failStep) {
        return new JobBuilder("exchangeSaveJob", jobRepository)
                .start(parsingStep)
                    .on("COMPLETED")
                    .to(saveStep)
                    .on("COMPLETED")
                    .to(successStep)
                    .on("COMPLETED")
                    .end()
                .from(successStep)
                    .on("*")
                    .to(failStep)
                .from(saveStep)
                    .on("*")
                    .to(failStep)
                .from(parsingStep)
                    .on("*")
                    .to(failStep)
                .end()
                .build();
    }

    @Bean
    public Step parsingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("parsingStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    String result = "COMPLETED";
//                    String result = "FAIL";
//                    String result = "UNKNOWN";

                    if (result.equals("COMPLETED"))
                        contribution.setExitStatus(ExitStatus.COMPLETED);
                    else if (result.equals("FAIL"))
                        contribution.setExitStatus(ExitStatus.FAILED);
                    else if (result.equals("UNKNOWN"))
                        contribution.setExitStatus(ExitStatus.UNKNOWN);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step saveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("saveStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    String result = "COMPLETED";
//                    String result = "FAIL";
//                    String result = "UNKNOWN";

                    if (result.equals("COMPLETED"))
                        contribution.setExitStatus(ExitStatus.COMPLETED);
                    else if (result.equals("FAIL"))
                        contribution.setExitStatus(ExitStatus.FAILED);
                    else if (result.equals("UNKNOWN"))
                        contribution.setExitStatus(ExitStatus.UNKNOWN);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step successStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("successStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    String result = "COMPLETED";
//                    String result = "FAIL";
//                    String result = "UNKNOWN";

                    if (result.equals("COMPLETED"))
                        contribution.setExitStatus(ExitStatus.COMPLETED);
                    else if (result.equals("FAIL"))
                        contribution.setExitStatus(ExitStatus.FAILED);
                    else if (result.equals("UNKNOWN"))
                        contribution.setExitStatus(ExitStatus.UNKNOWN);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step failStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("failStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    String result = "COMPLETED";
//                    String result = "FAIL";
//                    String result = "UNKNOWN";

                    if (result.equals("COMPLETED"))
                        contribution.setExitStatus(ExitStatus.COMPLETED);
                    else if (result.equals("FAIL"))
                        contribution.setExitStatus(ExitStatus.FAILED);
                    else if (result.equals("UNKNOWN"))
                        contribution.setExitStatus(ExitStatus.UNKNOWN);

                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
