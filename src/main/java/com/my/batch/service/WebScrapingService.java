package com.my.batch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import com.my.batch.exception.error.UndefinedRequestApiException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class WebScrapingService {

    private static final String URL = "https://m.stock.naver.com/marketindex/home/exchangeRate/exchange";

    private String getWebpage() {
        try {
            Document doc = Jsoup.connect(URL).get();
            return doc.html();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ExchangeScrapResponseDto> extractInformation() {
        String htmlContent = getWebpage();

        if (htmlContent == null) {
            return null;
        }

        try {
            Document doc = Jsoup.parse(htmlContent);
            Element scriptTag = doc.select("script#__NEXT_DATA__").first();

            if (scriptTag != null) {
                String jsonData = scriptTag.data();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(jsonData);
                JsonNode resultNode = rootNode.path("props")
                        .path("pageProps")
                        .path("dehydratedState")
                        .path("queries")
                        .get(2)
                        .path("state")
                        .path("data")
                        .path("result");
                return convertJsonToExchangeDto(resultNode);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedRequestApiException();
        }
        return null;
    }

    private List<ExchangeScrapResponseDto> convertJsonToExchangeDto(JsonNode jsonNode) {
        List<ExchangeScrapResponseDto> dtoList = new ArrayList<>();

        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String currencyCode = fieldNames.next();
            JsonNode currencyInfo = jsonNode.get(currencyCode);

            ExchangeScrapResponseDto dto = new ExchangeScrapResponseDto();
            dto.setUnit(currencyCode);
            dto.setName(currencyInfo.path("name").asText());
            dto.setKr_unit(currencyInfo.path("currencyName").asText());
            dto.setDeal_basr(currencyInfo.path("calcPrice").asDouble());

            dtoList.add(dto);
        }
        return dtoList;
    }

}