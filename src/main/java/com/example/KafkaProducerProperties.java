package com.example;

import lombok.Data;

import java.util.Map;

@Data
public class KafkaProducerProperties {

    private Integer retries;
    private Map<String, String> topics;
}
