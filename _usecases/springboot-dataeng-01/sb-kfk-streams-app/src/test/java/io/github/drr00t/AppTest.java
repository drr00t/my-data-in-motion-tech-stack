package io.github.drr00t;

import io.github.drr00t.filmcatalog.stream.WordCountProcessor;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;
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
@EmbeddedKafka(ports = {9095})
public class AppTest {

    @Test
    @DisplayName("StreamBuilder message forwarded")
    void givenInputMessages_whenProcessed_thenMessagesAreForwarded() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String,String> wordCountProcessor = streamsBuilder.stream("input-topic");
        wordCountProcessor.to("output-topic");
        Topology topology = streamsBuilder.build();
        var props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("input-topic", new StringSerializer(), new StringSerializer());

            TestOutputTopic<String, String> outputTopic = topologyTestDriver
                    .createOutputTopic("output-topic", new StringDeserializer(), new StringDeserializer());

            inputTopic.pipeInput("key", "hello world");
            inputTopic.pipeInput("key2", "hello");

            Assertions.assertThat(outputTopic.readKeyValuesToList())
                    .containsExactly(
                            KeyValue.pair("key", "hello world"),
                            KeyValue.pair("key2", "hello"));
        }
    }

    @Test
    @DisplayName("Persist message into local store")
    void givenInputmessages_whenMessagemBeenProcessed_thenPersistIntoLocalStore(){
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // Create a state store
        streamsBuilder.table("input-topic",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.as("local-store"));

        Topology topology = streamsBuilder.build();
        var props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("input-topic", new StringSerializer(), new StringSerializer());

            inputTopic.pipeInput("key", "persisted value");
            inputTopic.pipeInput("key", "persisted value 1");

            // Retrieve the local store and assert the latest value for the key
            var store = topologyTestDriver.getKeyValueStore("local-store");
            Assertions.assertThat(store.get("key")).isEqualTo("persisted value 1");
        }
    }
}