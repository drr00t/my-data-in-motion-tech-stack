package io.github.drr00t.dssamzacdc;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.List;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@EmbeddedKafka(ports = 9095)
public class AppTest {
    private static final String TEMPLATE_TOPIC = "enriched-user-data";

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    /**
     *
     *
     *
     * Rigorous Test :-)
     */
    @Test
    @DisplayName("New film received from Kafka")
    public void shouldAnswerWithTrue() {
        // Given
        Consumer<String, String> testConsumer = consumerFactory.createConsumer("test", "test");
        testConsumer.subscribe(List.of("sakila.public.film"));

        // When
        kafkaTemplate.send("sakila.public.film", "new UserData(userName, customerNumber)");

        // Then
        ConsumerRecord<String, String> receivedRecord = KafkaTestUtils.getSingleRecord(testConsumer, "sakila.public.film");
        Assertions.assertEquals("\"new UserData(userName, customerNumber)\"",receivedRecord.value());
    }
}