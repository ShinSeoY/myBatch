package com.my.batch.util;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.service.ExchangeService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExchangeUtilsTests {

    @Resource(name = "exchangeUtils")
    private ExchangeUtils exchangeUtils;

    @Resource(name = "exchangeService")
    private ExchangeService exchangeService;

    @Test
    void getExchangeDataAsDtoList() {

        // Given
        String date = "20240309000000";

        // When
        List<ExchangeWebApiResponseDto> res = exchangeUtils.getExchangeDataAsDtoList(date);

        // Then
        assertNotNull(res);
    }

    @Test
    void saveExchangeList() {

        // Given
        String date = "20240309000000";
        List<ExchangeWebApiResponseDto> given = exchangeUtils.getExchangeDataAsDtoList(date);

        // When
        exchangeService.saveExchangeList(given);

    }

}
