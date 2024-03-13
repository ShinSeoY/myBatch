package com.my.batch.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchSchedule {

    private final JobLauncher jobLauncher;
    private final Job exchangeSaveJob;

    @Scheduled(cron = "1 0 11 * * *") // 매일 아침 11시 0분 1초
    public void runJob() {
        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        try {
            log.info("scheduler is running ......{}", LocalDateTime.now());
            jobLauncher.run(exchangeSaveJob, parameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        }
    }
}
