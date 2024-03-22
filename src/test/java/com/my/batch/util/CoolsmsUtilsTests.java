package com.my.batch.util;

import com.my.batch.common.utils.CoolsmsUtils;
import com.my.batch.dto.coolsms.request.SendSMSRequestDto;
import jakarta.annotation.Resource;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CoolsmsUtilsTests {

    @Resource(name = "coolsmsUtils")
    private CoolsmsUtils coolsmsUtils;

    @Test
    void getExchangeDataAsDtoList() {

        // Given
        SendSMSRequestDto sendSMSRequestDto = SendSMSRequestDto.builder()
                .name("미국")
                .exchangeRate("1300.0")
                .krUnit("달러")
                .unit("USD")
                .build();

        String text = "예약 환율 알림 \n" +
                sendSMSRequestDto.getName() + "(" + sendSMSRequestDto.getUnit() + ") 매매 기준율 : " + sendSMSRequestDto.getExchangeRate() + " " + sendSMSRequestDto.getKrUnit();

        String to = "01057377038";

        // When
        SingleMessageSentResponse res = coolsmsUtils.sendSms(to, text);

        System.out.println(res);

        // Then
        assertNotNull(res);
    }

}
