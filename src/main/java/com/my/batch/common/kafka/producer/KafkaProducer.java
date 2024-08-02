package com.my.batch.common.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC = "notification";

    public void sendMessages(List<String> messages) {
        messages.forEach(message ->
                kafkaTemplate.send(TOPIC, message)
                        .whenComplete((result, ex) -> {
                                    if (ex == null) {
                                        handleSuccess(result, message);
                                    } else {
                                        handleFailure(ex, message);
                                    }
                                }
                        )
        );
    }

    private void handleSuccess(SendResult<String, String> result, String message) {
        log.info("Message sent successfully: {}", message);
        log.debug("Partition: {}, Offset: {}",
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }

    private void handleFailure(Throwable ex, String message) {
        log.error("Failed to send message: {}", message, ex);
    }
}