package com.my.batch.util;

import com.my.batch.common.utils.SmtpUtils;
import com.my.batch.dto.smtp.request.SendEmailRequestDto;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmtpUtilsTests {

    @Resource(name = "smtpUtils")
    private SmtpUtils smtpUtils;

    @Test
    void sendEmail() {

        // Given
        SendEmailRequestDto sendEmailRequestDto = SendEmailRequestDto.builder()
                .address("shsy312@naver.com")
                .title("test")
                .content("test 입니당")
                .build();

        // When
        smtpUtils.sendEmail(sendEmailRequestDto);

    }

}
