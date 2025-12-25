package com.smart.rabbit.websocket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.smart.rabbit.config.RabbitMQConfig;
import com.smart.rabbit.payload.RabbitMQMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQSocketHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final WebSocketPushService webSocketPushService;

	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		log.info("WebSocket connection established: {}", session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("Received message from {}: {}", session.getId(), message.getPayload());
		session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("WebSocket connection closed: {} Status: {}", session.getId(), status);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage(), exception);
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void consume(Message message, Channel channel) throws Exception {

		long tag = message.getMessageProperties().getDeliveryTag();

		try {
			RabbitMQMessage<? extends java.io.Serializable> event = objectMapper.readValue(message.getBody(),
					new TypeReference<RabbitMQMessage<? extends java.io.Serializable>>() {
					});
			log.info("Received RabbitMQ message {}", event);

			webSocketPushService.sendMessage(sessions, event);

			channel.basicAck(tag, false);

		} catch (Exception ex) {
			log.error("Failed to consume RabbitMQ message", ex);
			channel.basicNack(tag, false, true);
		}
	}
}