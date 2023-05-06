package com.example;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

public class KafkaContainersConfig {

    @RegisterExtension
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.2"));

    @PreDestroy
    public void stop() {
        kafka.stop();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            kafka.start();
            Map<String, String> properties = new HashMap<>();
            properties.put("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
            TestPropertyValues.of(properties)
                    .applyTo(applicationContext);
        }
    }
}
