package com.my.batch.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
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
    private final Job notificationJob;

    @Scheduled(cron = "0 0 2 * * *") //매일 아침 11시 0분 0초에 실행
    public void runExchangeSaveJob() {
        // 동일 job을 계속 실행하게 하기 위해 파라미터 설정
        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        try {
            log.info("scheduler is running ......{}", LocalDateTime.now());
            jobLauncher.run(exchangeSaveJob, parameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0-8 * * *") //매일 오전9-오후5시까지 한시간마다 한번씩 실행
    public void runNotificationJob() {
        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        try {
            log.info("scheduler is running ......{}", LocalDateTime.now());
            jobLauncher.run(notificationJob, parameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        }
    }
}
