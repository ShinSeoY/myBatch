package com.my.batch.util;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExchangeUtilsTests {

	private static final Logger log = LoggerFactory.getLogger(ExchangeUtils.class);

	@Resource(name = "exchangeUtils")
	private ExchangeUtils exchangeUtils;

	@Test
	void getExchangeDataAsDtoList() {

		// Given

		// When
		List<ExchangeWebApiResponseDto> res = exchangeUtils.getExchangeDataAsDtoList();

		// Then
		assertNotNull(res);
	}

}
