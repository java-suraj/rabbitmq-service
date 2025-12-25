package com.smart.rabbit.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.smart.rabbit.config.RabbitMQConfig;
import com.smart.rabbit.payload.ApiResponse;
import com.smart.rabbit.payload.EventType;
import com.smart.rabbit.payload.RabbitMQMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQFastProducerService {

	private final RabbitTemplate rabbitTemplate;

	/**
	 * Publishes a fast JSON message using Jackson + Blackbird.
	 *
	 * @param eventType logical event name
	 * @param source    producer service name
	 * @param payload   generic payload
	 * @param userId    optional userId (WebSocket routing)
	 */
	public <T extends java.io.Serializable> ApiResponse<String> publish(EventType eventType, String source, T payload,
			String userId) {

		try {
			String eventId = UUID.randomUUID().toString();

			RabbitMQMessage<T> message = RabbitMQMessage.<T>builder().eventId(eventId).eventType(eventType)
					.source(source).timestamp(Instant.now()).payload(payload).userId(userId).retryCount(0).version("v1")
					.build();

			rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message, msg -> {
				msg.getMessageProperties().setMessageId(eventId);
				msg.getMessageProperties().setContentType(MediaType.APPLICATION_JSON_VALUE);
				return msg;
			});

			log.info("RabbitMQ fast JSON message published | eventId={} | eventType={}", eventId, eventType);
			return ApiResponse.<String>builder().success(true).message("RabbitMQ fast JSON message published").code(202)
					.data(eventId).build();
		} catch (AmqpException e) {
			log.error("Failed to publish RabbitMQ fast JSON message | eventType={} | error={}", eventType,
					e.getMessage(), e);
			return ApiResponse.<String>builder().success(false).message("Failed to publish RabbitMQ fast JSON message")
					.code(500).build();
		}
	}

	/**
	 * Publishes a pre-constructed RabbitMQMessage.
	 *
	 * @param message pre-built RabbitMQMessage
	 */
	public <T extends java.io.Serializable> ApiResponse<String> publish(RabbitMQMessage<T> message) {

		try {
			rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message, msg -> {
				msg.getMessageProperties().setMessageId(message.getEventId());
				msg.getMessageProperties().setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
				return msg;
			});

			log.info("RabbitMQ message published | eventId={} | eventType={}", message.getEventId(),
					message.getEventType());
			return ApiResponse.<String>builder().success(true).message("RabbitMQ message published").code(202)
					.data(message.getEventId()).build();
		} catch (AmqpException e) {
			log.error("Failed to publish RabbitMQ message | eventId={} | eventType={} | error={}", message.getEventId(),
					message.getEventType(), e.getMessage(), e);
			return ApiResponse.<String>builder().success(false).message("Failed to publish RabbitMQ message").code(500)
					.build();
		}
	}

}