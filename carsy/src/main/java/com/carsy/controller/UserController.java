package com.carsy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entity.User;
import com.carsy.repository.UserRepository;


@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public int test() {
		return 1;
	}
	
	@GetMapping("/users")
	public List<User> getAll()
	{
		return userRepository.getAll();
	}
	
	@GetMapping("/users/{UserID}")
	public User getById(@PathVariable("UserID") int UserID)
	{
		return userRepository.getById(UserID);
	}
}