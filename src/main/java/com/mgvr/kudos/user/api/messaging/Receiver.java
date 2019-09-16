package com.mgvr.kudos.user.api.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgvr.kudos.user.api.constants.RabbitmqQueueNames;
import com.mgvr.kudos.user.api.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.mgvr.kudos.user.api.dao.UserDao;
import org.springframework.messaging.handler.annotation.SendTo;


public class Receiver {
	@Autowired
	private UserDao dao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

	@RabbitListener(queues =RabbitmqQueueNames.KUDO_RPC_USER_REQUEST)
    @SendTo(RabbitmqQueueNames.KUDO_RPC_KUDO_API)
	public String receiveUserQueryRequest(User message) throws JsonProcessingException {
		System.out.println("[Receiver] ha recibido el mensaje \"" + message.getRealName() + '"');
        User user = dao.getUserByRealName(message.getRealName());
        if(user!=null){
            ObjectMapper Obj = new ObjectMapper();
            String jsonStr = Obj.writeValueAsString(user);
            return jsonStr;
        }
        return null;
    }
}
