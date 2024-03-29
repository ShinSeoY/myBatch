package com.my.batch.service;

import com.my.batch.domain.Notification;
import com.my.batch.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> validPeriod() {
        List<Notification> notificationsForMsg = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByEnabled(true);

        notifications.stream().forEach((it) -> {
            // 3개월 기간 체크
            LocalDateTime afterThreeMonth = it.getCreatedAt().plusMonths(3);
            if (LocalDateTime.now().isAfter(afterThreeMonth)) {
                it.setEnabled(false);
                notificationRepository.save(it);
            } else {
                notificationsForMsg.add(it);
            }
        });
        return notificationsForMsg;
    }

}
