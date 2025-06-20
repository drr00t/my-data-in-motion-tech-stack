package io.github.drr00t.dssamzacdc.boundary;

import com.google.common.collect.ImmutableList;
import io.github.drr00t.dssamzacdc.boundary.descriptors.DebeziumInputDescriptor;
import io.github.drr00t.dssamzacdc.boundary.descriptors.DebeziumOutputDescriptor;
import io.github.drr00t.dssamzacdc.boundary.descriptors.DebeziumSystemDescriptor;
import org.apache.samza.application.TaskApplication;
import org.apache.samza.application.descriptors.TaskApplicationDescriptor;
import org.apache.samza.serializers.JsonSerdeV2;
import org.apache.samza.serializers.KVSerde;
import org.apache.samza.serializers.StringSerde;
import org.apache.samza.storage.kv.descriptors.RocksDbTableDescriptor;

public class CdcStreamTaskApplication implements TaskApplication {
    @Override
    public void describe(TaskApplicationDescriptor appDescriptor) {
        DebeziumSystemDescriptor dsd = new DebeziumSystemDescriptor("debezium");
//                .withConsumerZkConnect(ImmutableList.of("..."))
//                .withProducerBootstrapServers(ImmutableList.of("...", "..."));

        DebeziumInputDescriptor<String> did =
                dsd.getInputDescriptor("pageViewEvent", new JsonSerdeV2<>(String.class));
        DebeziumOutputDescriptor<String> dod =
                dsd.getOutputDescriptor("goodPageViewEvent", new JsonSerdeV2<>(String.class));

        RocksDbTableDescriptor<String,String> sakila_films =
                new RocksDbTableDescriptor<>("sakila_films", KVSerde.of(new StringSerde(), new StringSerde()));

        // Step 2: Add input, output streams and tables
        appDescriptor
                .withInputStream(did)
                .withOutputStream(dod)
                .withTable(sakila_films);

        // Step 3: define the processing logic
        appDescriptor.withTaskFactory(new CdcTaskApplicationFactory());
    }
}
