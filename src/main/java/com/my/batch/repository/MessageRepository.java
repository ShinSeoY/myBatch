package com.my.batch.repository;

import com.my.batch.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.phone = :phone AND m.content LIKE '%[' || :certificationMsg || ']%' ORDER BY m.createdAt DESC")
    Page<Message> findByCertification(String phone, String certificationMsg, Pageable pageable);
}
