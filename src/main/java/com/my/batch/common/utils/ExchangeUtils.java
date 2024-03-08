package com.my.batch.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.exception.error.ParsingException;
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

    private final String searchdate = getSearchdate();


    public List<ExchangeWebApiResponseDto> getExchangeDataAsDtoList() {
        JsonNode jsonNode = getExchangeDataSync();

        if (jsonNode != null && jsonNode.isArray()) {
            List<ExchangeWebApiResponseDto> exchangeWebApiResponseDtoList = new ArrayList<>();

            for (JsonNode node : jsonNode) {
                ExchangeWebApiResponseDto exchangeWebApiResponseDto = convertJsonToExchangeDto(node);
                exchangeWebApiResponseDtoList.add(exchangeWebApiResponseDto);
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

    private JsonNode getExchangeDataSync() {

//        DefaultUriBuilderFactory 객체를 생성하여 인코딩 모드를 None으로 변경하고 이를 아래와 같이 WebClient에 적용했습니다.
//        queryParam을 사용할 때, API를 WebClient로 호출하기 위해서 인코딩을 하지 않도록 처리하였습니다.
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        ResponseEntity<String> map = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(host)
                        .path("/" + path)
                        .queryParam("authkey", authkey)
                        .queryParam("searchdate", searchdate)
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

    private String getSearchdate() {

        LocalDateTime currentDate = LocalDateTime.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // 비영업일(토, 일, 오전11시 이전) 처리
        if (dayOfWeek.getValue() == 6) {     // 토요일
            currentDate = currentDate.minusDays(1).toLocalDate().atTime(LocalTime.MAX);
        } else if (dayOfWeek.getValue() == 7) {     // 일요일
            currentDate = currentDate.minusDays(2).toLocalDate().atTime(LocalTime.MAX);
            ;
        }
        if (currentDate.getHour() < 11) {        // 11시 이전
            currentDate = currentDate.minusDays(1).toLocalDate().atTime(LocalTime.MAX);
        }
        return currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
