package com.my.batch.repository;

import com.my.batch.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification findByMemberId(Integer memberId);


    @Modifying
    @Query("SELECT DISTINCT n FROM Notification n LEFT JOIN FETCH n.member LEFT JOIN FETCH n.exchange WHERE n.isEnabled = :isEnabled")
    List<Notification> findByEnabled(boolean isEnabled);
}
