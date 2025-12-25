package com.smart.rabbit.payload;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.rabbit.exception.RabbitMQMessageException;

import lombok.RequiredArgsConstructor;

/**
 * Deserializes JSON bytes into RabbitMQMessage<T>.
 */
@RequiredArgsConstructor
public class RabbitMQMessageDeserializer {

    private final ObjectMapper objectMapper;

    public <T extends java.io.Serializable> RabbitMQMessage<T> deserialize(
            byte[] body,
            TypeReference<RabbitMQMessage<T>> typeRef) {

        try {
            return objectMapper.readValue(body, typeRef);
        } catch (IOException ex) {
            throw new RabbitMQMessageException(
                    "Failed to deserialize RabbitMQ message",
                    ex,
                    null,
                    false
            );
        }
    }
}