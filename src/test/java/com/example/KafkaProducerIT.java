package com.example;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.kafka.topics=kafka-study-topic",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
@Import(KafkaConsumer.Config.class)
@ContextConfiguration(initializers = KafkaContainersConfig.Initializer.class)
class KafkaProducerIT {

    @Autowired
    private KafkaSender sender;

    @Autowired
    private KafkaConsumer consumer;

    @MockBean
    private KafkaEventHandler handler;

    @Test
    void should_send_kafka_message() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        sender.send("My kafka message");

        verify(handler, timeout(10000)).handle(captor.capture());
        String actual = captor.getValue();

        assertThat(actual).isNotBlank();
    }
}
