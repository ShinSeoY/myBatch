package com.my.batch.repository;

import com.my.batch.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification findByMemberId(Integer memberId);
}
