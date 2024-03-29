package com.my.batch.service;

import com.my.batch.domain.Notification;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationServiceTests {

    @Resource(name = "notificationService")
    private NotificationService notificationService;


    @Test
    public void validPeriod() {
        List<Notification> notifications = notificationService.validPeriod();

        assertThat(notifications).isNotEmpty();
    }
}
