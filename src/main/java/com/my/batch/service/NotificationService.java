package com.my.batch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.batch.common.kafka.producer.KafkaProducer;
import com.my.batch.common.msg.EventListenerHandler;
import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.constant.CalcType;
import com.my.batch.constant.MsgTemplateType;
import com.my.batch.constant.SendType;
import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import com.my.batch.dto.common.msg.EmailEvent;
import com.my.batch.dto.common.msg.SmsEvent;
import com.my.batch.dto.notification.response.ValidNotificationResponseDto;
import com.my.batch.exception.error.ParsingException;
import com.my.batch.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher publisher;
    private final CryptoDbUtil cryptoDbUtil;
    private final KafkaProducer kafkaProducer;
    private final EventListenerHandler eventListenerHandler;

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

    public void sendNotificationMsg(List<Notification> notifications) {
        notifications.stream().forEach((it) -> {
            Boolean isReachedStatus = false;
            int res = it.getGoalExchangeRate().compareTo(it.getExchange().getDealBasR());

            if (res == 0 || it.getCalcType().equals(CalcType.LTE) ? res > 0 : res < 0) {
                isReachedStatus = true;
            }
            if (isReachedStatus) {
                String text = getText(it);
                Message emailMessage = buildMessage(it, text);
                Message smsMessage = buildMessage(it, text);

                if (it.isEmailEnabled()) {
                    publisher.publishEvent(new EmailEvent(it, emailMessage));
                    it.setEnabled(false);
                }
                if (it.isSmsEnabled()) {
                    publisher.publishEvent(new SmsEvent(cryptoDbUtil, it, smsMessage, null));
                    it.setEnabled(false);
                }
                notificationRepository.save(it);
            }
        });
    }

    public void sendNotificationMsgFromKafka(ValidNotificationResponseDto validNotificationResponseDto) {
        Notification notification = notificationRepository.findById(validNotificationResponseDto.getNotificationId()).orElseThrow();

        Message emailMessage = buildMessage(notification, validNotificationResponseDto.getContent());
        Message smsMessage = buildMessage(notification, validNotificationResponseDto.getContent());

        if (notification.isEmailEnabled()) {
            eventListenerHandler.sendEmail(notification, emailMessage);
            notification.setEnabled(false);
        }
        if (notification.isSmsEnabled()) {
            eventListenerHandler.sendSms(notification, smsMessage);
            notification.setEnabled(false);
        }
        notificationRepository.save(notification);
    }

    public void sendNotificationMsgToKafka(List<Notification> notifications) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<String> messages = notifications.stream().filter(it -> {
                    double calcDealBasR = it.getExchange().getDisplayUnit() != null ? it.getExchange().getDisplayUnit() * it.getExchange().getDealBasR() : it.getExchange().getDealBasR();
                    int res = it.getGoalExchangeRate().compareTo(calcDealBasR);
                    if (it.getCalcType().equals(CalcType.LTE)) {
                        return res >= 0;
                    } else if (it.getCalcType().equals(CalcType.GTE)) {
                        return res <= 0;
                    } else {
                        return false;
                    }
                }
        ).map((it) -> {
            String text = getText(it);
            Map<String, Object> message = buildMessageForProducer(it, text);
            try {
                return mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                log.error(e.toString());
                throw new ParsingException();
            }
        }).collect(Collectors.toList());

        kafkaProducer.sendMessages(messages);
    }

    private Map<String, Object> buildMessageForProducer(Notification notification, String text) {
        return Map.of(
                "notificationId", notification.getId(),
                "content", text
        );
    }

    private Message buildMessage(Notification notification, String text) {
        return Message.builder()
                .memberId(notification.getMember().getId())
                .email(notification.getMember().getEmail())
                .phone(notification.getMember().getPhone())
                .content(text)
                .msgTemplateType(MsgTemplateType.RESERVATION)
                .sendStatus(SendType.SENDING)
                .build();
    }

    public String getText(Notification notification) {
        return "예약 환율 알림 \n" +
                notification.getExchange().getName() + "(" + notification.getExchange().getUnit() + ") 매매 기준율 : " + notification.getExchange().getDealBasR() + " " + notification.getExchange().getKrUnit();
    }

}
