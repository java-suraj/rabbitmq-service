package com.smart.rabbit.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketRegistryConfig implements WebSocketConfigurer {

    private final RabbitMQSocketHandler rabbitMQSocketHandler;
    private final HttpHandshakeInterceptor httpHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(rabbitMQSocketHandler, "/ws/raw/rabbitmq")
                .addInterceptors(httpHandshakeInterceptor)
                .setAllowedOrigins(
                        "http://localhost:19010",
                        "http://127.0.0.1:19010"
                );
    }
}
