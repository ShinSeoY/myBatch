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

    private boolean isProcess = true;

//    @Scheduled(cron = "0 1 11 * * *") //매일 아침 11시 0분 0초에 실행
    @Scheduled(cron = "0 */5 * * * *")
    // kafka listener가 듣고 있으므로 아예 해당 job이 필요 없어짐
    public void runExchangeSaveJob() {
        executeJob(exchangeSaveJob);
    }

    @Scheduled(cron = "0 0 9-17 * * *") //매일 오전9-오후5시까지 한시간마다 한번씩 실행
    public void runNotificationJob() {
        executeJob(notificationJob);
    }

    private void executeJob(Job job) {
        if (!isProcess) {
            return;
        }

        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        try {
            LocalDateTime startTime = LocalDateTime.now();
            log.info("Scheduler is running job '{}' at {}.", job.getName(), startTime);
            jobLauncher.run(job, parameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error("Failed to run job '{}': {}", job.getName(), e.getMessage());
        }
    }
}
