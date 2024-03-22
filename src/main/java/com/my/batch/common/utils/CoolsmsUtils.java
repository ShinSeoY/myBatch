package com.my.batch.common.utils;

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
        return response;
    }

//    public void sendVerificationMessage(String to, LocalDateTime sentAt){
//        Message message = new Message();
//        message.setFrom(smsSender);
//        message.setTo(to);
//
//        VerificationCode verificationCode = VerificationCodeGenerator
//                .generateVerificationCode(sentAt);
//        verificationCodeRepository.save(verificationCode);
//
//        String text = verificationCode.generateCodeMessage();
//        message.setText(text);
//
//        messageService.sendOne(new SingleMessageSendingRequest(message));
//    }
//
//    public void verifyCode(String code, LocalDateTime verifiedAt){
//        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
//                .orElseThrow(() -> new GeneralException(ErrorStatus._VERIFICATION_CODE_NOT_FOUND));
//
//        if(verificationCode.isExpired(verifiedAt)){
//            throw new GeneralException(ErrorStatus._VERIFICATION_CODE_EXPIRED);
//        }
//
//        verificationCodeRepository.remove(verificationCode);
//    }
}
