package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(String event) {
        log.info("Send kafka message={}", event);

        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, event);
            kafkaTemplate.send(producerRecord);
        } catch (Exception e) {
            throw new KafkaException(String.format("Problem on sending message to kafka topic=%s", topic), e);
        }
    }
}
