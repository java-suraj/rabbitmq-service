# RabbitMQ WebSocket Service

A lightweight Spring Boot service that forwards messages from RabbitMQ to connected WebSocket (STOMP) clients in real time. It provides a REST publishing endpoint and a WebSocket/STOMP broker so backend producers can publish to RabbitMQ and clients (browser, Postman, React, etc.) can receive updates instantly.

---

## Highlights

- RabbitMQ producer & consumer integration (Spring AMQP)
- WebSocket with STOMP support (no SockJS)
- Broadcast and user-specific messaging
- Optimized Jackson configuration (Blackbird + JSR-310)
- Java 21, Spring Boot 3.x compatible
- Docker Compose for local RabbitMQ during development

---

## Quick start (local)

Prerequisites: Java 21 JDK, Docker & Docker Compose, Maven/Gradle

1) Start RabbitMQ for local development:

```bash
docker compose up -d
```

RabbitMQ management UI: http://localhost:15672

2) Build and run the service:

```bash
./mvnw clean package
java -jar target/*.jar
```

Default app URL: http://localhost:19010

---

## Endpoints & WebSocket

- WebSocket (STOMP) endpoint: ws://localhost:19010/ws/rabbitmq
- Example STOMP subscribe destination: /topic/orders
- Application prefix used by controllers: /app/*

---

## REST publish API

Publish a message (the service forwards this to RabbitMQ):

POST http://localhost:19010/api/rabbit/publish

Sample JSON body:

```json
{
  "eventType": "ORDER_CREATED",
  "payload": {
    "orderId": "ORD-123",
    "amount": 5000
  }
}
```

cURL example:

```bash
curl -X POST http://localhost:19010/api/rabbit/publish \
  -H "Content-Type: application/json" \
  -d '{"eventType":"ORDER_CREATED","payload":{"orderId":"ORD-123","amount":5000}}'
```

---

## Postman WebSocket test (raw STOMP frames)

CONNECT
accept-version:1.2
host:localhost

^@

SUBSCRIBE
id:sub-1
destination:/topic/orders

^@

Trigger a publish via the REST API and messages should be delivered to subscribed clients.

---

## Configuration

Check src/main/resources/application.yml (or application.properties) for exact keys. Typical properties to set:

- spring.rabbitmq.host, spring.rabbitmq.port, spring.rabbitmq.username, spring.rabbitmq.password
- server.port (19010 by default)
- websocket endpoint/path (e.g. /ws/rabbitmq)

---

## Production notes

- Secure WebSocket endpoints (JWT/OAuth) and validate incoming messages.
- Use durable queues and persistent messages for critical flows.
- Add Dead Letter Exchanges/Queues for failed processing.
- Configure RabbitMQ clustering / high-availability for production.
- Monitor consumers and message rates; consider backpressure strategies.

---

## Project layout (high level)

- src/main/java — controllers, services, RabbitMQ listener/producer, WebSocket config
- src/main/resources — application.yml/properties, static files
- docker-compose.yml — RabbitMQ for local development

---

## Contributing

Contributions are welcome. Please open an issue or PR and follow standard Java + Spring Boot practices.

---

## Author

Suraj Kumar Singh
Senior Java Backend Engineer

---

## License

MIT License
