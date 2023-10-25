package com.carsy.controllers;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import com.carsy.entities.User;
import com.carsy.repositories.UserRepository;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public int test() {
		return 1;
	}
	
	@GetMapping("")
	public List<User> getAll()
	{
		return userRepository.getAll();
	}
	
	@GetMapping("/{UserID}")
	public User getById(@PathVariable("UserID") int UserID)
	{
		return userRepository.getById(UserID);
	}
	
	

	
	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
	@PostMapping("")
	public ResponseEntity<String> add(@RequestBody List<User> users) 
	{
	    for (User user : users) 
	    {
	    	if (!EMAIL_VALIDATOR.isValid(user.getEmail())) 
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Nieprawidłowy adres e-mail"); // Adres e-mail jest nieprawidłowy
	    		
	    }
	   
	    
	    	userRepository.save(users);
	        return ResponseEntity.status(HttpStatus.OK).body("Udało sie");
	}
	
}