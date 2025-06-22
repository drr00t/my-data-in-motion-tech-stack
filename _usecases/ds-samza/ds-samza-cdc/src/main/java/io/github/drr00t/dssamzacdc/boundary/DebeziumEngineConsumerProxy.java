package io.github.drr00t.dssamzacdc.boundary;

import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebeziumEngineConsumerProxy {
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumEngineConsumerProxy.class);
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> engine;

    public DebeziumEngineConsumerProxy(Configuration config, DebeziumCdcSystemConsumer cdcSystemConsumer) {
        config.asProperties().setProperty("name", cdcSystemConsumer.getConsumerSystemName());
        engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(config.asProperties())
                .notifying(cdcSystemConsumer)
                .build();

    }

    public void start() {
        engine.run();
    }

    public void stop() {
        try {
            engine.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to close Debezium engine", e);
        }
    }
}

