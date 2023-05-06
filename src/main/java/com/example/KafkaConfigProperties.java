package com.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaConfigProperties {

    private List<String> bootstrapServers;
    @NestedConfigurationProperty
    private KafkaProducerProperties producer;
}
