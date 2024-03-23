package com.my.batch.repository;

import com.my.batch.domain.Notification;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationRepositoryTests {

    @Resource(name = "notificationRepository")
    private NotificationRepository notificationRepository;

    @Test
    void generateJwtToken() {

        // Given
        Integer email = 1;

        // When
        Notification notification = notificationRepository.findByMemberId(email);

        System.out.println(notification.getId());

        // Then
        assertNotNull(notification);
    }

}
