package com.mgvr.kudos.user.api.controller;

import java.util.List;

import com.mgvr.kudos.user.api.com.mgvr.kudos.user.api.constants.ApiMessages;
import com.mgvr.kudos.user.api.com.mgvr.kudos.user.api.constants.RabbitmqExchangeName;
import com.mgvr.kudos.user.api.com.mgvr.kudos.user.api.constants.RabbitmqRoutingKeys;
import com.mgvr.kudos.user.api.com.mgvr.kudos.user.api.constants.UserApiRoutes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgvr.kudos.user.api.dao.UserDao;
import com.mgvr.kudos.user.api.model.User;

@RestController
@RequestMapping(UserApiRoutes.BASE_ROUTE)
public class UserController {
	@Autowired
	private UserDao dao;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostMapping(UserApiRoutes.POST_USER)
	public String saveUser(@RequestBody User user) {
		dao.saveUser(user);
		return ApiMessages.USER_CREATED;
	}

	@GetMapping(UserApiRoutes.GET_USERS)
	public List<User> getAllUsers() {
		return dao.getAllUsers();
	}
	
	@GetMapping(UserApiRoutes.GET_USER_BY_ID)
	public User getUser(@PathVariable String id) {
		return dao.getUser(Integer.parseInt(id));
	}
	
	@PutMapping(UserApiRoutes.PUT_USER)
	public String updateUser(@PathVariable String id, @RequestBody User user) {
		user.setId(Integer.parseInt(id));
		dao.updateUser(user);
		return ApiMessages.USER_UPDATED;
	}
	
	@DeleteMapping(UserApiRoutes.DELETE_USER)
	public String deleteUser(@PathVariable String id) {
		User user = dao.getUser(Integer.parseInt(id));
		String responseDeleteKudos = (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_KUDO_DELETE_REQUEST, user);
		if(responseDeleteKudos.equalsIgnoreCase(ApiMessages.KUDOS_DELETED)){
			dao.deleteUser(user);
			return ApiMessages.USER_DELETED;
		}
		dao.deleteUser(user);
		return ApiMessages.USER_NOT_DELETED;
	}
}
