package io.github.drr00t;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.VersionedBytesStoreSupplier;
import org.apache.kafka.streams.state.VersionedKeyValueStore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
@EmbeddedKafka(ports = {9095})
public class TopologyStreamTest {

    @Test
    @DisplayName("Hello word topology")
    void givenInputMessages_whenProcessed_thenMessagesAreForwarded() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // serdes
        // json Serde
//        final Serializer<JsonNode> jsonSerializer = new JsonSerde<>() JsonSerializer<>();
//        final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer<>();
//        final Serde<JsonNode> jsonSerde = new JsonSerde<>(JsonNode.class);//Serdes.serdeFrom(jsonSerializer, jsonDeserializer);


        //source stream
        KStream<String, String> wordCountSource = streamsBuilder
                .stream("input-topic", Consumed.with(Serdes.String(), Serdes.String()));

        //processor stream
        KStream<String, String> wordCountProcessor = wordCountSource
                .mapValues(value -> value.toUpperCase());

        //sink processor
        wordCountProcessor.to("output-topic", Produced.with(Serdes.String(), Serdes.String()));

        Topology topology = streamsBuilder.build();

        var props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
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
                            KeyValue.pair("key", "hello world".toUpperCase()),
                            KeyValue.pair("key2", "hello".toUpperCase()));
        }
    }

    @Test
    @DisplayName("Persist Key value store message into local store")
    void givenInputmessages_whenMessagemBeenProcessed_thenPersistIntoLocalStore() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // Create a state store
        streamsBuilder.table("input-topic",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.as("local-store"));

        Topology topology = streamsBuilder.build();
        var props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("input-topic", new StringSerializer(), new StringSerializer());

            inputTopic.pipeInput("key", "persisted value");
            inputTopic.pipeInput("key", "persisted value 1");

            // Retrieve the local store and assert the latest value for the key
            ReadOnlyKeyValueStore<String, String> store = topologyTestDriver.getKeyValueStore("local-store");
            try (var itr = store.all()) {
                while (itr.hasNext()) {
                    KeyValue<String, String> next = itr.next();
                }
            }

            Assertions.assertThat(store.get("key")).isEqualTo("persisted value 1");
        }
    }

    @Test
    @DisplayName("Persist versioned message into local store")
    void givenInputmessages_whenMessagemBeenProcessed_thenVersionedPersistIntoLocalStore() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // Create a state store
        // Using VersionedKeyValueStore to persist the latest version of the value
        VersionedBytesStoreSupplier versionedStoreSupplier = Stores
                .persistentVersionedKeyValueStore("local-store-versioned", Duration.ofMinutes(1));
        KTable<String, String> kWords = streamsBuilder.table("input-topic",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.<String, String>as(versionedStoreSupplier)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));

        kWords.toStream().print(Printed.<String, String>toSysOut().withLabel("Output stream"));

        Topology topology = streamsBuilder.build();
        var props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app-versioned");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("input-topic", new StringSerializer(), new StringSerializer());

            inputTopic.pipeInput("key", "persisted value");
            inputTopic.pipeInput("key", "persisted value 1");

            // Retrieve the local store and assert the latest value for the key
            VersionedKeyValueStore<String, String> store = topologyTestDriver.getVersionedKeyValueStore("local-store-versioned");
//            var query = new Query();
//            var itr = store.get("key");
//            var next = itr.value();

            Assertions.assertThat(store.get("key").value()).isEqualTo("persisted value 1");
        }
    }

    @Test
    @DisplayName("Query versioned message from local store")
    void givenInputmessages_whenMessagemBeenProcessed_thenQueryVersionedPersistIntoLocalStore() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // Create a state store
        // Using VersionedKeyValueStore to persist the latest version of the value
        VersionedBytesStoreSupplier versionedStoreSupplier = Stores
                .persistentVersionedKeyValueStore("query-local-store-versioned", Duration.ofMinutes(1));

        Map<String, String> changeLogConfigs = Map.of(
                "retention.ms", "172800000", // 2 days
                "retention.bytes", "10 000 000 000" // 10GB
        );

        KTable<String, String> kWords = streamsBuilder.table("query-input-topic",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.<String, String>as(versionedStoreSupplier)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String())
                        .withLoggingEnabled(changeLogConfigs)
        );

        kWords.toStream().print(Printed.<String, String>toSysOut().withLabel("Output stream"));

        Topology topology = streamsBuilder.build();
        var props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "query-word-count-app-versioned");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver
                    .createInputTopic("query-input-topic", new StringSerializer(), new StringSerializer());

            inputTopic.pipeInput("key", "persisted value");
            Thread.sleep(1000);
            inputTopic.pipeInput("key", "persisted value 1");
            inputTopic.pipeInput("key", "persisted value 1");

            // Retrieve the local store and assert the latest value for the key
            VersionedKeyValueStore<String, String> store = topologyTestDriver.getVersionedKeyValueStore("query-local-store-versioned");

            Assertions.assertThat(store.get("key").value()).isEqualTo("persisted value 1");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}