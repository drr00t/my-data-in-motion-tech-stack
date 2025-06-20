package io.github.drr00t.dssamzacdc.boundary;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// This is the actualy cdc executor that will process the messages
public class CdcStreamTask implements StreamTask {

    private static final Logger LOG = LoggerFactory.getLogger(CdcStreamTask.class);

    @Override
    public void process(
            IncomingMessageEnvelope envelope,
            MessageCollector collector,
            TaskCoordinator coordinator) {

        // process the message in the envelope synchronously
    }
}
