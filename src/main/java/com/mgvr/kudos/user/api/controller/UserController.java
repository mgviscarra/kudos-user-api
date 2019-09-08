package com.mgvr.kudos.user.api.controller;

import java.util.List;

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
@RequestMapping("/kudos-user-api")
public class UserController {
	@Autowired
	private UserDao dao;

	@PostMapping("/user")
	public String saveUser(@RequestBody User user) {
		dao.saveUser(user);
		return "success";
	}

	@GetMapping("/user")
	public List<User> getAllUsers() {
		return dao.getAllUsers();
	}
	
	@PutMapping("/user/{id}")
	public String updateUser(@PathVariable String id, @RequestBody User user) {
		user.setId(Integer.parseInt(id));
		dao.updateUser(user);
		return "success";
	}
	
	@DeleteMapping("/user/{id}")
	public String deleteUser(@PathVariable String id) {
		User user = new User();
		user.setId(Integer.parseInt(id));
		dao.deleteUser(user);
		return "success";
	}
}
