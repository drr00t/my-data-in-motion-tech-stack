package io.github.drr00t.dssamzacdc.boundary;

import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DebeziumCdcSystemConsumer
        implements DebeziumEngine.ChangeConsumer<RecordChangeEvent<SourceRecord>> {

    private static final Logger LOG = LoggerFactory.getLogger(DebeziumCdcSystemConsumer.class);

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
}
