package com.my.batch.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "batch_status")
public class BatchStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String dagName;

    String jobName;

    String status; // COMPLETED, FAILED, ..

    long duration; // 밀리초

    LocalDateTime startTime; // step의 시작 시간

    LocalDateTime endTime; // step의 종료 시간

    String failedStepName;

    @Column(columnDefinition = "TEXT")
    String errMsg;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

}
