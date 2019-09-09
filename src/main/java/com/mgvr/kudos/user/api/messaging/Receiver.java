package com.mgvr.kudos.user.api.messaging;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


public class Receiver {

	
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value="test"),
			exchange = @Exchange(value="test"),
			key = "test"
		))
	public void receiveMessage(String message) {
        System.out.println("[Receiver] ha recibido el mensaje \"" + message + '"');
    }
}
