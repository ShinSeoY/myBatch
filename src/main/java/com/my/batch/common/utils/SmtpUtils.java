package com.my.batch.common.utils;

import com.my.batch.dto.smtp.request.SendEmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpUtils {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender emailSender;

    public void sendEmail(SendEmailRequestDto sendEmailRequestDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(sendEmailRequestDto.getAddress());
        message.setSubject(sendEmailRequestDto.getTitle());
        message.setText(sendEmailRequestDto.getContent());

        try {
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
