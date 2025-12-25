package com.smart.rabbit.websocket;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.rabbit.payload.RabbitMQMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketPushService {

    private final ObjectMapper objectMapper;

    public void sendMessage(Set<WebSocketSession> sessions, RabbitMQMessage<?> event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new org.springframework.web.socket.TextMessage(payload));
                    log.debug("Sent message to session={} eventId={}", session.getId(), event.getEventId());
                }
            }
        } catch (Exception e) {
            log.error("Error sending message eventId={}: {}", event.getEventId(), e.getMessage(), e);
        }
    }
}