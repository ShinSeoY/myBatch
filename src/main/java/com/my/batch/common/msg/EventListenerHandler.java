package com.my.batch.common.msg;

import com.my.batch.common.utils.CoolsmsUtils;
import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.common.utils.SmtpUtils;
import com.my.batch.constant.MsgType;
import com.my.batch.constant.SendType;
import com.my.batch.domain.Message;
import com.my.batch.domain.Notification;
import com.my.batch.dto.common.msg.EmailEvent;
import com.my.batch.dto.common.msg.SmsEvent;
import com.my.batch.dto.smtp.request.SendEmailRequestDto;
import com.my.batch.exception.error.SendMsgFailErrorException;
import com.my.batch.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListenerHandler {

    private final MessageRepository messageRepository;
    private final CoolsmsUtils coolsmsUtils;
    private final SmtpUtils smtpUtils;
    private final CryptoDbUtil cryptoDbUtil;

    @Async
    @EventListener
    public void sendEmailWithListener(EmailEvent emailEvent) {
        try {
            SendEmailRequestDto sendEmailRequestDto = SendEmailRequestDto.builder()
                    .address(emailEvent.getNotification().getMember().getEmail())
                    .title("오늘의 환율 알림 서비스")
                    .content(emailEvent.getMessage().getContent())
                    .build();
            smtpUtils.sendEmail(sendEmailRequestDto);

            emailEvent.getMessage().setMsgType(MsgType.EMAIL);
            emailEvent.getMessage().setSendResult(SendType.SEND_SUCCESS);
        } catch (SendMsgFailErrorException e) {
            emailEvent.getMessage().setSendResult(SendType.FAILED);
            emailEvent.getMessage().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            emailEvent.getMessage().setSendResult(SendType.FAILED);
            emailEvent.getMessage().setErrorMsg(e.getMessage());
        } finally {
            messageRepository.save(emailEvent.getMessage());
        }
    }

    @Async
    @EventListener
    public void sendSmsWithListener(SmsEvent smsEvent) {
        try {
            String to;
            if (smsEvent.getNotification() != null) {
                to = smsEvent.getNotification().getMember().getPlainPhone();
            } else {
                to = smsEvent.getMessage().getPlainPhone();
            }
            coolsmsUtils.sendSms(to, smsEvent.getMessage().getContent());

            smsEvent.getMessage().setMsgType(MsgType.SMS);
            smsEvent.getMessage().setSendResult(SendType.SEND_SUCCESS);
        } catch (SendMsgFailErrorException e) {
            smsEvent.getMessage().setSendResult(SendType.FAILED);
            smsEvent.getMessage().setErrorMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            smsEvent.getMessage().setSendResult(SendType.FAILED);
            smsEvent.getMessage().setErrorMsg(e.getMessage());
        } finally {
            messageRepository.save(smsEvent.getMessage());
        }
    }

    public void sendEmail(Notification notification, Message message) {
        try {
            SendEmailRequestDto sendEmailRequestDto = SendEmailRequestDto.builder()
                    .address(notification.getMember().getEmail())
                    .title("오늘의 환율 알림 서비스")
                    .content(message.getContent())
                    .build();
            smtpUtils.sendEmail(sendEmailRequestDto);

            message.setMsgType(MsgType.EMAIL);
            message.setSendResult(SendType.SEND_SUCCESS);
        } catch (SendMsgFailErrorException e) {
            message.setSendResult(SendType.FAILED);
            message.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            message.setSendResult(SendType.FAILED);
            message.setErrorMsg(e.getMessage());
        } finally {
            messageRepository.save(message);
        }
    }

    public void sendSms(Notification notification, Message message) {
        try {
            String plainPhone = cryptoDbUtil.decrypt(notification.getMember().getPhone());
            coolsmsUtils.sendSms(plainPhone, message.getContent());

            message.setMsgType(MsgType.SMS);
            message.setSendResult(SendType.SEND_SUCCESS);
        } catch (SendMsgFailErrorException e) {
            message.setSendResult(SendType.FAILED);
            message.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            message.setSendResult(SendType.FAILED);
            message.setErrorMsg(e.getMessage());
        } finally {
            messageRepository.save(message);
        }
    }
}
