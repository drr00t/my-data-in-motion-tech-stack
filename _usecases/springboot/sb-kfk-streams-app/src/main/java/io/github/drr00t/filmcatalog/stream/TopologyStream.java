package io.github.drr00t.filmcatalog.stream;

public class TopologyStream {
    private final TopicStream<String, String> INPUT_WORDS;
    private final TopicStream<String, String> OUTPUT_WORDS_COUNT;

    public TopologyStream(TopicStream<String, String> inputWords, TopicStream<String, String> outputWordsCount) {
        this.INPUT_WORDS = inputWords;
        this.OUTPUT_WORDS_COUNT = outputWordsCount;
    }
}
