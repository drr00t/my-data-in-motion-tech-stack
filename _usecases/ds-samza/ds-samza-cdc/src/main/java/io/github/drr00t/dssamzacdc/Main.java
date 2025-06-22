package io.github.drr00t.dssamzacdc;

import io.github.drr00t.dssamzacdc.boundary.CdcStreamTaskApplication;
import joptsimple.OptionSet;
import org.apache.samza.config.Config;
import org.apache.samza.config.MapConfig;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.util.CommandLine;

import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CommandLine cmdLine = new CommandLine();
        Map<String,String> props = new HashMap<>();


        props.put("app.name","word-count");
        props.put("job.coordinator.factory","org.apache.samza.standalone.PassthroughJobCoordinatorFactory");
        props.put("job.coordination.utils.factory","org.apache.samza.standalone.PassthroughCoordinationUtilsFactory");
        props.put("job.default.system","kafka");
        props.put("task.name.grouper.factory","org.apache.samza.container.grouper.task.SingleContainerGrouperFactory");
        props.put("processor.id","0");
        props.put("systems.kafka.default.stream.samza.offset.default","oldest");

        props.put("debezium.name", "samza-debezium-connector");
        props.put("debezium.connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.put("debezium.offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.put("debezium.offset.storage.file.filename", "/tmp/offsets.dat");
        props.put("debezium.offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.put("debezium.database.hostname", "localhost");
        props.put("debezium.database.port", "5432");
        props.put("debezium.database.user", "postgres");
        props.put("debezium.database.password", "postgres");
        props.put("debezium.plugin.name", "pgoutput");
        props.put("debezium.database.dbname", "postgres");
        props.put("debezium.table.whitelist", "public.film");
        props.put("debezium.topic.prefix", "samza-debezium");
//        props.put("debezium.table.whitelist", "public.film,public.actor,public.film_actor,public.film_category,public.category");

        Config config =  new MapConfig(props);
        LocalApplicationRunner runner = new LocalApplicationRunner(new CdcStreamTaskApplication(), config);
        runner.run();
        runner.waitForFinish();
    }
}