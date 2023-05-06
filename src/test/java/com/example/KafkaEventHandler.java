package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventHandler.class);

    public void handle(String msg) {
        LOGGER.info("Handle received kafka message: " + msg);
    }
}
