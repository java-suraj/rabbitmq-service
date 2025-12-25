package com.smart.rabbit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.rabbit.payload.ApiResponse;
import com.smart.rabbit.payload.RabbitMQMessage;
import com.smart.rabbit.payload.RabbitPublishRequest;
import com.smart.rabbit.service.RabbitMQFastProducerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rabbit")
@RequiredArgsConstructor
public class RabbitMQProducerController {

	private final RabbitMQFastProducerService producerService;

	/**
	 * 1️⃣ Basic publish (no group, no user)
	 */
	@PostMapping("/publish")
	public <T extends java.io.Serializable> ApiResponse<String> publish(
			@Valid @RequestBody RabbitPublishRequest<T> request) {

		return producerService.publish(request.getEventType(), request.getSource(), request.getPayload(), null);

	}

	/**
	 * 2️⃣ Publish to a GROUP (ADMIN / USER / AUDIT)
	 */
	@PostMapping("/publish/group")
	public <T extends java.io.Serializable> ResponseEntity<Void> publishToGroup(
			@Valid @RequestBody RabbitPublishRequest<T> request) {

		RabbitMQMessage<T> message = RabbitMQMessage.createMessageWithGroup(request.getEventType(), request.getSource(),
				request.getPayload(), request.getGroup());

		producerService.publish(message);

		return ResponseEntity.accepted().build();
	}

	/**
	 * 3️⃣ Publish to a specific USER
	 */
	@PostMapping("/publish/user")
	public <T extends java.io.Serializable> ApiResponse<String> publishToUser(
			@Valid @RequestBody RabbitPublishRequest<T> request) {

		RabbitMQMessage<T> message = RabbitMQMessage.createMessageWithUserId(request.getEventType(),
				request.getSource(), request.getPayload(), request.getUserId());

		return producerService.publish(message);
	}
}
