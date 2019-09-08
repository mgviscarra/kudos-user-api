package com.mgvr.kudos.user.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgvr.kudos.user.api.dao.UserDao;
import com.mgvr.kudos.user.api.model.User;

@RestController
@RequestMapping("/kudos-user-api")
public class UserController {
	@Autowired
	private UserDao dao;

	@PostMapping("/saveUser")
	public String save(@RequestBody User user) {
		dao.saveUser(user);
		return "success";
	}

	@GetMapping("/getAllUser")
	public List<User> getALlPersons() {
		return dao.getUser();
	}
}
