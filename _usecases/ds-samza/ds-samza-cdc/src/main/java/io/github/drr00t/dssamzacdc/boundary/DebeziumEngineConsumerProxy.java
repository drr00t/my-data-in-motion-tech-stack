package io.github.drr00t.dssamzacdc.boundary;

import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.samza.config.Config;
import org.apache.samza.util.BlockingEnvelopeMap;

import java.util.List;
import java.util.Properties;

public class DebeziumEngineConsumerProxy extends BlockingEnvelopeMap{
    DebeziumEngine<RecordChangeEvent<SourceRecord>> engine;
    public DebeziumEngineConsumerProxy(String systemName, Configuration config, DebeziumCdcSystemConsumer cdcSystemConsumer) {
        config.asProperties().setProperty("name", generateUniqueStreamName(systemName));
        engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(config.asProperties())
                .notifying(cdcSystemConsumer)
                .build();

    }

    @Override
    public void start() {
        engine.run();
    }

    @Override
    public void stop() {
        try {
        engine.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to close Debezium engine", e);
        }
    }

    public static String generateUniqueStreamName(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID();
    }
}
