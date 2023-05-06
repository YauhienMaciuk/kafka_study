package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private final KafkaEventHandler kafkaEventHandler;

    public KafkaConsumer(KafkaEventHandler kafkaEventHandler) {
        this.kafkaEventHandler = kafkaEventHandler;
    }

    @KafkaListener(topics = "${spring.kafka.topics}")
    public void consume(String record) {
        kafkaEventHandler.handle(record);
    }

    @TestConfiguration
    public static class Config {

        @Bean
        public ConsumerFactory<String, Object> consumerFactory(KafkaConfigProperties kafkaConfigProperties) {
            Map<String, Object> properties = new HashMap<>();

            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            return new DefaultKafkaConsumerFactory<>(properties);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory) {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);

            return factory;
        }
    }
}
