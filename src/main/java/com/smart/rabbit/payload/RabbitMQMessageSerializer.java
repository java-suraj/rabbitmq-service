package com.smart.rabbit.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.rabbit.exception.RabbitMQMessageException;

import lombok.RequiredArgsConstructor;

/**
 * Serializes RabbitMQMessage<T> to JSON bytes.
 */
@RequiredArgsConstructor
public class RabbitMQMessageSerializer {

    private final ObjectMapper objectMapper;

    public byte[] serialize(RabbitMQMessage<?> message) {

        try {
            return objectMapper.writeValueAsBytes(message);
        } catch (JsonProcessingException ex) {
            throw new RabbitMQMessageException(
                    "Failed to serialize RabbitMQ message",
                    ex,
                    message != null ? message.getEventId() : null,
                    false
            );
        }
    }
}
