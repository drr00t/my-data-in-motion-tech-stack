package io.github.drr00t.dssamzacdc.boundary;

import io.debezium.config.Configuration;
import org.apache.samza.config.Config;

import java.util.Objects;
import java.util.Properties;

public class DebeziumConfiguration {
    // This class will encapsulate the debezium configuration
    // and provide methods to create a debezium engine consumer proxy
    // from the samza config

    public static Configuration from(Config config) {
        Objects.requireNonNull(config);

        Properties debeziumProperties = new Properties();
        config.forEach((key, value) -> {
            if (key.startsWith("debezium.")) {
                debeziumProperties.put(key.substring("debezium.".length()), value);
            }
        });
        return Configuration.from(debeziumProperties);
    }
}
