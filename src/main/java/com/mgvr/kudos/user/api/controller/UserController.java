package com.mgvr.kudos.user.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgvr.kudos.user.api.constants.*;
import com.mgvr.kudos.user.api.model.Kudo;
import com.monitorjbl.json.JsonResult;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = dao.getAllUsers();
		JsonResult json = JsonResult.instance();
        List<User> listUsers= json.use(JsonView.with(users)
                .onClass(User.class, Match.match()
                        .exclude(DbFields.REAL_NAME)
                        .exclude(DbFields.EMAIL)
                        .exclude(DbFields.KUDOS)
                                )).returnValue();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}
	
	@GetMapping(UserApiRoutes.GET_USER)
	public ResponseEntity<?> getUser(@RequestParam Map<String,String> allParams) throws IOException {
        if (allParams.size() == 0 || allParams.size() > 1) {
            return ResponseEntity.badRequest().body(ApiMessages.GET_USER_REQUIRED_PARAMETERS);
        }
        User user = null;
        switch (allParams.entrySet().iterator().next().getKey().toString()) {
            case DbFields.ID:
                user = dao.getUser(Integer.parseInt(allParams.get(DbFields.ID)));
                break;
            case DbFields.REAL_NAME:
                user = dao.getUserByRealName(allParams.get(DbFields.REAL_NAME));
                break;
            case DbFields.NICK_NAME:
                user = dao.getUserByNickName(allParams.get(DbFields.NICK_NAME));
                break;
        }
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        String responseKudosUser=  (String)rabbitTemplate.convertSendAndReceive
                (RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_GET_KUDO_FOR_USER_REQUEST, user);
        ObjectMapper mapper = new ObjectMapper();
        List<Kudo> kudoList = mapper.readValue(responseKudosUser, mapper.getTypeFactory().constructCollectionType(List.class, Kudo.class));
        user.setKudos(kudoList);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

	
	@PutMapping(UserApiRoutes.PUT_USER)
	public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User user) {
		user.setId(Integer.parseInt(id));
		dao.updateUser(user);
        return new ResponseEntity<>(ApiMessages.USER_UPDATED, HttpStatus.OK);
	}
	
	@DeleteMapping(UserApiRoutes.DELETE_USER)
	public ResponseEntity<String> deleteUser(@PathVariable String id) {
		User user = dao.getUser(Integer.parseInt(id));
		String responseDeleteKudos = (String)rabbitTemplate.convertSendAndReceive
				(RabbitmqExchangeName.EXCHANGE_NAME, RabbitmqRoutingKeys.KUDO_RPC_KUDO_DELETE_REQUEST, user);
		if(responseDeleteKudos.equalsIgnoreCase(ApiMessages.KUDOS_DELETED)){
			dao.deleteUser(user);
            return new ResponseEntity<>(ApiMessages.USER_DELETED, HttpStatus.OK);
		}
        return new ResponseEntity<>(ApiMessages.USER_NOT_DELETED, HttpStatus.CONFLICT);
	}

}
