package com.my.batch.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "exchange")
public class Exchange implements Serializable {

    @Id
    String unit;

    String name;

    String krUnit;

    Double dealBasR;

    @Setter
    Integer displayUnit;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "exchange", fetch = FetchType.LAZY)
    private Set<MemberExchange> memberExchanges;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "exchange", fetch = FetchType.LAZY)
    private Set<Notification> notifications;

}
