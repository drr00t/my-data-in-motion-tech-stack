package io.github.drr00t.dssamzacdc.boundary.descriptors;

import org.apache.samza.serializers.Serde;
import org.apache.samza.system.descriptors.OutputDescriptor;
import org.apache.samza.system.descriptors.SystemDescriptor;

public class DebeziumOutputDescriptor<StreamMessageType>
        extends OutputDescriptor<StreamMessageType, DebeziumOutputDescriptor<StreamMessageType>> {
    DebeziumOutputDescriptor(String streamId, SystemDescriptor systemDescriptor, Serde<StreamMessageType> serde) {
        super(streamId, serde, systemDescriptor);
    }
}
