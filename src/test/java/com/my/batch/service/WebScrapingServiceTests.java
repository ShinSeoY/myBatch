package com.my.batch.service;

import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WebScrapingServiceTests {

    @Resource(name = "webScrapingService")
    private WebScrapingService webScrapingService;


    @Test
    public void validPeriod() {
        List<ExchangeScrapResponseDto> extractedData = webScrapingService.extractInformation();
        if (extractedData != null) {
            System.out.println(extractedData);
        } else {
            System.out.println("Failed to extract data from the webpage.");
        }
    }
}
