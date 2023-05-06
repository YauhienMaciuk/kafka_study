package com.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaConfig {

    private static final String TOPIC_NAME = "kafka-study-topic";

    private final KafkaConfigProperties kafkaConfigProperties;

    public KafkaConfig(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = producerConfigs();

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaSender kafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        KafkaProducerProperties producerProperties = kafkaConfigProperties.getProducer();
        Map<String, String> topics = producerProperties.getTopics();
        String topic = topics.get(TOPIC_NAME);

        return new KafkaSender(kafkaTemplate, topic);
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        KafkaProducerProperties producerProperties = kafkaConfigProperties.getProducer();
        List<String> bootstrapServers = kafkaConfigProperties.getBootstrapServers();
        Integer retriesConfig = producerProperties.getRetries();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.RETRIES_CONFIG, retriesConfig);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return properties;
    }
}
