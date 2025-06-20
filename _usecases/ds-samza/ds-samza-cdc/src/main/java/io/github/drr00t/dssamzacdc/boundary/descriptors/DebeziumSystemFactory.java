package io.github.drr00t.dssamzacdc.boundary.descriptors;
import io.github.drr00t.dssamzacdc.boundary.DebeziumCdcSystemConsumer;
import io.github.drr00t.dssamzacdc.boundary.DebeziumConfiguration;
import io.github.drr00t.dssamzacdc.boundary.DebeziumEngineConsumerProxy;
import org.apache.samza.SamzaException;
import org.apache.samza.util.SinglePartitionWithoutOffsetsSystemAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.samza.config.Config;
import org.apache.samza.metrics.MetricsRegistry;
import org.apache.samza.system.SystemAdmin;
import org.apache.samza.system.SystemConsumer;
import org.apache.samza.system.SystemFactory;
import org.apache.samza.system.SystemProducer;

public class DebeziumSystemFactory implements SystemFactory{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public SystemConsumer getConsumer(String s, Config config, MetricsRegistry metricsRegistry) {
        return getConsumer(s, config, metricsRegistry, s);
    }

    @Override
    public SystemProducer getProducer(String s, Config config, MetricsRegistry metricsRegistry) {
        return getProducer(s, config, metricsRegistry, s);
    }

    @Override
    public SystemAdmin getAdmin(String s, Config config) {
        return getAdmin(s, config, s);
    }

    @Override
    public SystemConsumer getConsumer(String systemName, Config config, MetricsRegistry registry, String consumerLabel) {

        var debeziumCdcSystemConsumer = new DebeziumCdcSystemConsumer();
        var debeziumConfig = DebeziumConfiguration.from(config);
        return new DebeziumEngineConsumerProxy(systemName, debeziumConfig, debeziumCdcSystemConsumer);
    }

    @Override
    public SystemProducer getProducer(String systemName, Config config, MetricsRegistry registry, String producerLabel) {
        throw new SamzaException("You can't produce.");
    }

    @Override
    public SystemAdmin getAdmin(String systemName, Config config, String adminLabel) {
        return new SinglePartitionWithoutOffsetsSystemAdmin();
    }
}
