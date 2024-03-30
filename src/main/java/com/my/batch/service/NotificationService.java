package com.my.batch.service;

import com.my.batch.common.utils.CoolsmsUtils;
import com.my.batch.common.utils.SmtpUtils;
import com.my.batch.constant.CalcType;
import com.my.batch.domain.Notification;
import com.my.batch.dto.smtp.request.SendEmailRequestDto;
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
    private final CoolsmsUtils coolsmsUtils;
    private final SmtpUtils smtpUtils;

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

    public void sendMsg(List<Notification> notifications) {
        notifications.stream().forEach((it) -> {
            Boolean isReachedStatus = false;
            int res = it.getGoalExchangeRate().compareTo(it.getExchange().getDealBasR());

            if (res == 0 || it.getCalcType().equals(CalcType.LTE) ? res > 0 : res < 0) {
                isReachedStatus = true;
            }
            if (isReachedStatus && it.isEmailEnabled()) {
                sendEmail(it);
                it.setEnabled(false);
            }
            if (isReachedStatus && it.isSmsEnabled()) {
                sendSms(it);
                it.setEnabled(false);
            }
            notificationRepository.save(it);
        });
    }

    private void sendEmail(Notification notification) {
        SendEmailRequestDto sendEmailRequestDto = SendEmailRequestDto.builder()
                .address(notification.getMember().getEmail())
                .title("오늘의 환율 알림 서비스")
                .content(getText(notification))
                .build();
        smtpUtils.sendEmail(sendEmailRequestDto);
    }

    private void sendSms(Notification notification) {
        String to = notification.getMember().getPhone();

        coolsmsUtils.sendSms(to, getText(notification));
    }

    private String getText(Notification notification) {
        return "예약 환율 알림 \n" +
                notification.getExchange().getName() + "(" + notification.getExchange().getUnit() + ") 매매 기준율 : " + notification.getExchange().getExchangeRate() + " " + notification.getExchange().getKrUnit();
    }

}
