package drr00t;

import io.github.drr00t.filmcatalog.stream.WordCountProcessor;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
@EmbeddedKafka(ports = 9095, topics = {"sakila.public.film", "sakila.public.film_actor", "sakila.public.film_category", "sakila.public.film_text"})
public class AppTest {

    @Test
    @DisplayName("Streaming for Word count")
    void givenInputMessages_whenProcessed_thenWordCountIsProduced() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        var wordCountProcessor = new KafkaStreams(streamsBuilder, new Properties());
        Topology topology = streamsBuilder.build();

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, new Properties())) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("input-topic", new StringSerializer(), new StringSerializer());

            TestOutputTopic<String, Long> outputTopic = topologyTestDriver
                    .createOutputTopic("output-topic", new StringDeserializer(), new LongDeserializer());

            inputTopic.pipeInput("key", "hello world");
            inputTopic.pipeInput("key2", "hello");

            Assertions.assertThat(outputTopic.readKeyValuesToList())
                    .containsExactly(
                            KeyValue.pair("hello", 1L),
                            KeyValue.pair("world", 1L),
                            KeyValue.pair("hello", 2L)
                    );
        }
    }

    @Configuration
    @EnableKafkaStreams
    public static class TestKafkaStreamsConfiguration {

        @Value("${" + EmbeddedKafkaBroker.SPRING_EMBEDDED_KAFKA_BROKERS + "}")
        private String brokerAddresses;

        @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
        public KafkaStreamsConfiguration kStreamsConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddresses);
            return new KafkaStreamsConfiguration(props);
        }

    }
}