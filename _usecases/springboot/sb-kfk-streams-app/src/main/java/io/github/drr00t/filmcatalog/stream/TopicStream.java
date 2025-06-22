package io.github.drr00t.filmcatalog.stream;

public class TopicStream<KeySerde,ValueSerde> {
    public KeySerde keySerde;
    public ValueSerde valueSerde;
    public String topicStreamName;
    public TopicStream(KeySerde keySerde, ValueSerde valueSerde, String topicName) {
        this.keySerde = keySerde;
        this.valueSerde = valueSerde;
        this.topicStreamName = topicName;
    }
    public KeySerde getKeySerde() {
        return keySerde;
    }
    public ValueSerde getValueSerde() {
        return valueSerde;
    }
    public String getTopicStreamName() {
        return topicStreamName;
    }

    public static <KeySerde, ValueSerde> TopicStream<KeySerde, ValueSerde> of(KeySerde keySerde, ValueSerde valueSerde, String topicName) {
        return new TopicStream<>(keySerde, valueSerde, topicName);
    }
}
