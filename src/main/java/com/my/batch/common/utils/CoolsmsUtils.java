package com.my.batch.common.utils;

import com.my.batch.exception.error.SendMsgFailErrorException;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolsmsUtils {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.sender}")
    private String apiSender;
    @Value("${coolsms.provider}")
    private String apiProvider;


    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, apiProvider);
    }

    public SingleMessageSentResponse sendSms(String to, String text) {
        Message message = new Message();
        message.setFrom(apiSender);
        message.setTo(to);
        message.setText(text);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        if (!response.getStatusCode().equals("2000")) {
            throw new SendMsgFailErrorException();
        }
        return response;
    }

}
