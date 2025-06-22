package io.github.drr00t.filmcatalogjob.boundary.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class FilmConsumer {
    private final Logger logger = LoggerFactory.getLogger(FilmConsumer.class);

    @KafkaListener(topics = "sakila.public.film",groupId = "film-catalog-job")
    public void consume(String value,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        logger.info(String.format("\n\n Consumed event from topic %s: key = %-10s value = %s \n\n", topic, key, value));

    }

}


