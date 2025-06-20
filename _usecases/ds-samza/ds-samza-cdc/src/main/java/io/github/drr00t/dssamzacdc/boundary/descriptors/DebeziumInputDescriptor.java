package io.github.drr00t.dssamzacdc.boundary.descriptors;

import org.apache.samza.serializers.Serde;
import org.apache.samza.system.descriptors.InputDescriptor;
import org.apache.samza.system.descriptors.InputTransformer;
import org.apache.samza.system.descriptors.SystemDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DebeziumInputDescriptor<StreamMessageType>
        extends InputDescriptor<StreamMessageType, DebeziumInputDescriptor<StreamMessageType>> {
    private static final String CONSUMER_AUTO_OFFSET_RESET_CONFIG_KEY = "systems.%s.streams.%s.consumer.auto.offset.reset";
    private static final String CONSUMER_FETCH_MESSAGE_MAX_BYTES_CONFIG_KEY = "systems.%s.streams.%s.consumer.fetch.message.max.bytes";

    private Optional<String> consumerAutoOffsetResetOptional = Optional.empty();
    private Optional<Long> consumerFetchMessageMaxBytesOptional = Optional.empty();

    DebeziumInputDescriptor(String streamId, SystemDescriptor systemDescriptor, Serde serde, InputTransformer transformer) {
        super(streamId, serde, systemDescriptor, transformer);
    }

    @Override
    public Map<String, String> toConfig() {
        Map<String, String> configs = new HashMap<>(super.toConfig());

        // Note: Kafka configuration needs the topic's physical name, not the stream-id.
        // We won't have that here if user only specified it in configs, or if it got rewritten
        // by the planner to something different than what's in this descriptor.
        String streamName = getPhysicalName().orElse(getStreamId());
        String systemName = getSystemName();

        consumerAutoOffsetResetOptional.ifPresent(autoOffsetReset ->
                configs.put(String.format(CONSUMER_AUTO_OFFSET_RESET_CONFIG_KEY, systemName, streamName), autoOffsetReset));
        consumerFetchMessageMaxBytesOptional.ifPresent(fetchMessageMaxBytes ->
                configs.put(String.format(CONSUMER_FETCH_MESSAGE_MAX_BYTES_CONFIG_KEY, systemName, streamName), Long.toString(fetchMessageMaxBytes)));

        return configs;
    }
}
