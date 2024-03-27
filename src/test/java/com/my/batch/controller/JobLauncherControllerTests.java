package com.my.batch.controller;

import com.my.batch.dto.common.JobLaunchRequest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JobLauncherControllerTests {

    @Resource(name = "jobLauncherController")
    private JobLauncherController jobLauncherController;


    @Test
    @DisplayName("REST 방식으로 잡 실행하기 테스트 - exchangeSaveJob")
    public void runExchangeSaveJobTest() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        JobLaunchRequest request = new JobLaunchRequest();
        request.setName("exchangeSaveJob");
        request.setJobParameters(parameters);

        ExitStatus exitStatus = jobLauncherController.runJob(request);

        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @DisplayName("REST 방식으로 잡 실행하기 테스트 - notificationJob")
    public void runNotificationJobTest() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();
        JobLaunchRequest request = new JobLaunchRequest();
        request.setName("notificationJob");
        request.setJobParameters(parameters);

        ExitStatus exitStatus = jobLauncherController.runJob(request);

        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
    }
}
