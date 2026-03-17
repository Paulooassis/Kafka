package com.crud.kafka;

import com.mongodb.internal.logging.LogMessage;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Property;

@KafkaClient
public interface KafkaProducer {

    @Topic("${kafka.topics.error}")
    void sendErrorLog(@KafkaKey String key, byte[] log);

    @Topic("${kafka.topics.warn-info}")
    void sendWarnOrInfoLog(@KafkaKey String key, byte[] log);
}
