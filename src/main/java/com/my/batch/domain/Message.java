package com.my.batch.domain;

import com.my.batch.constant.MsgType;
import com.my.batch.constant.SendType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "massage")
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer memberId;

    @Enumerated(EnumType.STRING)
    private MsgType msgType;

    private String email;

    private String phone;

    private String content;

    @Enumerated(EnumType.STRING)
    private SendType sendStatus;

    @Enumerated(EnumType.STRING)
    private SendType sendResult;

    @Column(columnDefinition = "longtext")
    private String errorMsg;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

}
