package com.smart.rabbit.exception;

/**
 * Generic exception for RabbitMQ message processing failures.
 * Used in producer, consumer, retry, and DLQ handling.
 */
public class RabbitMQMessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final boolean retryable;

    public RabbitMQMessageException(
            String message,
            String eventId,
            boolean retryable) {
        super(message);
        this.eventId = eventId;
        this.retryable = retryable;
    }

    public RabbitMQMessageException(
            String message,
            Throwable cause,
            String eventId,
            boolean retryable) {
        super(message, cause);
        this.eventId = eventId;
        this.retryable = retryable;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
