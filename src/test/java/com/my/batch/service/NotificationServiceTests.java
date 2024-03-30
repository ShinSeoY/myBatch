package com.my.batch.service;

import com.my.batch.domain.Notification;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class NotificationServiceTests {

    @Resource(name = "notificationService")
    private NotificationService notificationService;


    @Test
    public void validPeriod() {
        List<Notification> notifications = notificationService.validPeriod();

        assertThat(notifications).isNotEmpty();
    }

    @Test
    public void sendMsg() {
        // Given
        List<Notification> notifications = notificationService.validPeriod();

        // When
        notificationService.sendMsg(notifications);

        assertThatNoException();
    }
}
