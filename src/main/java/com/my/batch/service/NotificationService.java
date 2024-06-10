package com.my.batch.service;

import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.constant.CalcType;
import com.my.batch.constant.MsgTemplateType;
import com.my.batch.constant.SendType;
import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import com.my.batch.dto.common.msg.EmailEvent;
import com.my.batch.dto.common.msg.SmsEvent;
import com.my.batch.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher publisher;
    private final CryptoDbUtil cryptoDbUtil;

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
                notification.getExchange().getName() + "(" + notification.getExchange().getUnit() + ") 매매 기준율 : " + notification.getExchange().getExchangeRate() + " " + notification.getExchange().getKrUnit();
    }

}
