package com.smart.rabbit.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Generic REST request for publishing RabbitMQ messages.
 */
@Data
public class RabbitPublishRequest<T> {

    @NotNull
    private EventType eventType;

    @NotNull
    private String source;

    @NotNull
    private T payload;

    private String userId;
    private Group group;
}
