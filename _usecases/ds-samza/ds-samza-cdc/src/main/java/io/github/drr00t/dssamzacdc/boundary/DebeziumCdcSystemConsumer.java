package io.github.drr00t.dssamzacdc.boundary;

import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;

import org.apache.samza.config.Config;
import org.apache.samza.metrics.MetricsRegistry;
import org.apache.samza.util.BlockingEnvelopeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DebeziumCdcSystemConsumer  extends BlockingEnvelopeMap
        implements DebeziumEngine.ChangeConsumer<RecordChangeEvent<SourceRecord>> {

    private static final Logger LOG = LoggerFactory.getLogger(DebeziumCdcSystemConsumer.class);
    DebeziumEngineConsumerProxy debeziumEngine;
    String consumerName;

    public DebeziumCdcSystemConsumer(String systemName, MetricsRegistry registry, Config config ) {
        super(registry);
        var debeziumConfig = DebeziumConfiguration.from(config);
        this.consumerName = generateUniqueStreamName(systemName);
        this.debeziumEngine = new DebeziumEngineConsumerProxy(debeziumConfig,this);;
    }

    @Override
    public void handleBatch(List<RecordChangeEvent<SourceRecord>> changes, DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) throws InterruptedException {
        for (RecordChangeEvent<SourceRecord> change : changes) {
            LOG.info("Key = '%s' value = '%s'".formatted(change.record().key(), change.record().value()));

            recordCommitter.markProcessed(change);
        }
    }

    @Override
    public boolean supportsTombstoneEvents() {
        return false;
    }


    @Override
    public void start() {
        debeziumEngine.start();
    }

    @Override
    public void stop() {
        debeziumEngine.stop();
    }

    public String generateUniqueStreamName(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID();
    }

    public String getConsumerSystemName() {
        return consumerName;
    }
}
