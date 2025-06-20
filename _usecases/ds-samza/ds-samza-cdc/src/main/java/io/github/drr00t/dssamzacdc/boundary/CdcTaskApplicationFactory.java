package io.github.drr00t.dssamzacdc.boundary;


import org.apache.samza.task.StreamTask;
import org.apache.samza.task.StreamTaskFactory;

public class CdcTaskApplicationFactory implements StreamTaskFactory {
    @Override
    public StreamTask createInstance() {
        return new CdcStreamTask();
    }
}