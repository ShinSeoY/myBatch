package com.my.batch.common.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import com.my.batch.exception.error.ParsingException;
import com.my.batch.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ExchangeService exchangeService;

    @KafkaListener(topics = "my-topic5", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        if (record.value() != null) {
            ExchangeScrapResponseDto exchangeScrapResponseDto = convertJsonToExchangeDto(record.value());
            exchangeService.saveExchange(exchangeScrapResponseDto);
        }

        // 메시지 처리가 성공적으로 완료되면 ack를 호출
        ack.acknowledge();
    }

    private ExchangeScrapResponseDto convertJsonToExchangeDto(String jsonValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonValue, ExchangeScrapResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ParsingException();
        }
    }
}
