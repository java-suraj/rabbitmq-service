# RabbitMQ WebSocket Service

A production-ready Spring Boot service that bridges **RabbitMQ** messages to **WebSocket (STOMP)** clients in real time.

This service allows backend systems to publish messages to RabbitMQ and instantly push them to connected WebSocket clients (browser, Postman, React, etc.).

---

## ✨ Features

- RabbitMQ producer & consumer
- Plain **Text WebSocket** (no SockJS)
- STOMP protocol support
- Real-time message delivery
- Broadcast & user-specific messaging
- High-performance Jackson configuration
- Java 21 + Spring Boot 3.x compatible
- Docker-ready RabbitMQ setup

---

## 🧱 Architecture

Client (Postman / React / Browser)
        |
        | WebSocket (STOMP)
        v
Spring Boot WebSocket Broker
        |
        | push
        v
RabbitMQ Consumer
        |
        | consume
        v
RabbitMQ Exchange / Queue

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring AMQP (RabbitMQ)
- Spring WebSocket (STOMP)
- Jackson (Blackbird + JSR310)
- RabbitMQ (Docker)

---

## 🚀 Getting Started

### Start RabbitMQ

```bash
docker compose up -d
```

RabbitMQ Management UI:
http://localhost:15672

---

## Application Port

http://localhost:19010

---

## 🔌 WebSocket Endpoint

ws://localhost:19010/ws/rabbitmq

---

## STOMP Destinations

Subscribe:
/topic/orders

Application Prefix:
/app/*

---

## 🧪 Postman WebSocket Test

CONNECT:
CONNECT
accept-version:1.2
host:localhost

^@

SUBSCRIBE:
SUBSCRIBE
id:sub-1
destination:/topic/orders

^@

---

## REST Publish API

POST http://localhost:19010/api/rabbit/publish

Sample Body:
{
  "eventType": "ORDER_CREATED",
  "payload": {
    "orderId": "ORD-123",
    "amount": 5000
  }
}

---

## Production Notes

- Use Text WebSocket for low latency
- Add DLQ for RabbitMQ
- Secure WebSocket with JWT
- Avoid manual JSON parsing

---

## Author

Suraj Kumar Singh
Senior Java Backend Engineer

---

## License

MIT License
