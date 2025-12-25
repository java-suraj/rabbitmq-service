package com.smart.rabbit.payload;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // Event identification
    private String eventId;
    private EventType eventType;
    private String version;

    // Producer information
    private String producerService;
    private String source;

    // Timing fields
    @Builder.Default
    private Instant timestamp = Instant.now();
    private Instant expiresAt;

    // Distributed tracing fields
    private String correlationId;
    private String traceId;
    private String spanId;

    // Multi-tenant and user context
    private String tenantId;
    private String userId;
    private Group group;

    // Message classification
    @Builder.Default
    private Priority priority = Priority.MEDIUM;
    private Category category;

    // Generic payload
    private T payload;

    // Retry mechanism
    private int retryCount;

    // Additional metadata
    private Map<String, Object> headers;
    
    
    public static <T> RabbitMQMessage<T> createBasicMessage(EventType eventType, String source, T payload) {
		return RabbitMQMessage.<T>builder()
				.eventId(java.util.UUID.randomUUID().toString())
				.eventType(eventType)
				.source(source)
				.timestamp(Instant.now())
				.payload(payload)
				.retryCount(0)
				.version("v1")
				.build();
	}
    
	public static <T> RabbitMQMessage<T> createMessageWithGroup(EventType eventType, String source, T payload, Group group) {
		return RabbitMQMessage.<T>builder()
				.eventId(java.util.UUID.randomUUID().toString())
				.eventType(eventType)
				.source(source)
				.timestamp(Instant.now())
				.payload(payload)
				.group(group)
				.retryCount(0)
				.version("v1")
				.build();
	}
	
	public static <T> RabbitMQMessage<T> createMessageWithUserId(EventType eventType, String source, T payload, String userId) {
		return RabbitMQMessage.<T>builder()
				.eventId(java.util.UUID.randomUUID().toString())
				.eventType(eventType)
				.source(source)
				.timestamp(Instant.now())
				.payload(payload)
				.userId(userId)
				.retryCount(0)
				.version("v1")
				.build();
	}
	
}