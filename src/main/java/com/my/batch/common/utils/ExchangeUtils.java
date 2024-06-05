package com.my.batch.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.exception.error.ExceededMaximumRequestsException;
import com.my.batch.exception.error.ParsingException;
import com.my.batch.exception.error.UndefinedRequestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ExchangeUtils {

    @Value("${exchange.authkey}")
    private String authkey;

    @Value("${exchange.type}")
    private String type;

    @Value("${exchange.host}")
    private String host;

    @Value("${exchange.path}")
    private String path;

    RestClient restClient = RestClient.create();

    public List<ExchangeWebApiResponseDto> getExchangeDataAsDtoList(String date) {
        JsonNode jsonNode = getExchangeDataSync(date);

        if (jsonNode != null && jsonNode.isArray()) {
            List<ExchangeWebApiResponseDto> exchangeWebApiResponseDtoList = new ArrayList<>();

            for (JsonNode node : jsonNode) {
                ExchangeWebApiResponseDto exchangeWebApiResponseDto = convertJsonToExchangeDto(node);
                if(exchangeWebApiResponseDto.getResult() == 1){
                    exchangeWebApiResponseDtoList.add(exchangeWebApiResponseDto);
                } else if (exchangeWebApiResponseDto.getResult() == 4){ // 일일제한횟수 마감
                    throw new ExceededMaximumRequestsException();
                } else { // 2 : DATA 코드 오류  3 : 인증코드 오류
                    throw new UndefinedRequestApiException();
                }
            }
            return exchangeWebApiResponseDtoList;
        }
        return Collections.emptyList();
    }

    private ExchangeWebApiResponseDto convertJsonToExchangeDto(JsonNode jsonNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.treeToValue(jsonNode, ExchangeWebApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ParsingException();
        }
    }

    private JsonNode getExchangeDataSync(String date) {
        ResponseEntity<String> map = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(host)
                        .path("/" + path)
                        .queryParam("authkey", authkey)
                        .queryParam("searchdate", getSearchdate(date))
                        .queryParam("data", type)
                        .build()
                )
                .retrieve()
                .toEntity(String.class);
        return parseJson(map.getBody());
    }

    private JsonNode parseJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new ParsingException();
        }
    }

    private String getSearchdate(String date) {

        LocalDateTime currentDate = date == null ? LocalDateTime.now() : LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // 비영업일(토, 일, 오전11시 이전) 처리
        if (dayOfWeek.getValue() == 6) {     // 토요일
            currentDate = currentDate.minusDays(1).toLocalDate().atTime(LocalTime.MAX);
        } else if (dayOfWeek.getValue() == 7) {     // 일요일
            currentDate = currentDate.minusDays(2).toLocalDate().atTime(LocalTime.MAX);
        }
        if (currentDate.getHour() < 11) {        // 11시 이전
            currentDate = currentDate.minusDays(1).toLocalDate().atTime(LocalTime.MAX);
        }
        return currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
