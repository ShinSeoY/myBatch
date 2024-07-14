package com.my.batch.common.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "your-topic-name", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        System.out.println("Received message: " + record.value());
        System.out.println("Topic: " + record.topic());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());

        // 메시지 처리 로직
        // ...

        // 메시지 처리가 성공적으로 완료되면 ack를 호출
        ack.acknowledge();
    }
}
