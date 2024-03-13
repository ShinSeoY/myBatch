package com.my.batch.domain;

import jakarta.persistence.*;
import lombok.*;
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

    String jobName;

    String status; // COMPLETED, FAILED, ..

    LocalDateTime startTime; // step의 시작 시간

    LocalDateTime endTime; // step의 종료 시간

    String failStepName;

    @Column(columnDefinition = "TEXT")
    String errMsg;

}
