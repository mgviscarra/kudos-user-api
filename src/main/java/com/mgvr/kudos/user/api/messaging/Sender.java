package com.mgvr.kudos.user.api.messaging;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.mgvr.kudos.user.api.KudosUserApiApplication;


public class Sender {
	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	public Sender(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	public void send() {
		System.out.println("Sending message...");
		 rabbitTemplate.convertAndSend(KudosUserApiApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
		 try {
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
