package io.github.drr00t.dssamzacdc.boundary.descriptors;

import org.apache.samza.serializers.Serde;
import org.apache.samza.system.descriptors.*;

public class DebeziumSystemDescriptor extends SystemDescriptor<DebeziumSystemDescriptor>
        implements SimpleInputDescriptorProvider, OutputDescriptorProvider {
    private static final String FACTORY_CLASS_NAME = DebeziumSystemFactory.class.getName();
//    private static final String CONSUMER_ZK_CONNECT_CONFIG_KEY = "systems.%s.consumer.zookeeper.connect";
//    private static final String CONSUMER_AUTO_OFFSET_RESET_CONFIG_KEY = "systems.%s.consumer.auto.offset.reset";
//    private static final String CONSUMER_FETCH_THRESHOLD_CONFIG_KEY = KafkaConfig.CONSUMER_FETCH_THRESHOLD();
//    private static final String CONSUMER_FETCH_THRESHOLD_BYTES_CONFIG_KEY = KafkaConfig.CONSUMER_FETCH_THRESHOLD_BYTES();
//    private static final String CONSUMER_FETCH_MESSAGE_MAX_BYTES_KEY = "systems.%s.consumer.fetch.message.max.bytes";
//    private static final String CONSUMER_CONFIGS_CONFIG_KEY = "systems.%s.consumer.%s";
//    private static final String PRODUCER_BOOTSTRAP_SERVERS_CONFIG_KEY = "systems.%s.producer.bootstrap.servers";
//    private static final String PRODUCER_CONFIGS_CONFIG_KEY = "systems.%s.producer.%s";

    public DebeziumSystemDescriptor(String systemName) {
        super(systemName, FACTORY_CLASS_NAME, null, null);
    }

    @Override
    public <StreamMessageType> DebeziumOutputDescriptor<StreamMessageType> getOutputDescriptor(String streamId, Serde<StreamMessageType> serde) {

        return new DebeziumOutputDescriptor<>(streamId, this, serde);
    }

    @Override
    public <StreamMessageType> DebeziumInputDescriptor<StreamMessageType> getInputDescriptor(String streamId, Serde<StreamMessageType> serde) {
        return new DebeziumInputDescriptor<>(streamId, this, serde, null);
    }
}
