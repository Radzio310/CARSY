package com.carsy.controllers;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.jdbc.core.JdbcTemplate;

import com.carsy.entities.User;
import com.carsy.repositories.UserRepository;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
	
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
	
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody User user) 
	{
	    
	    	if (!EMAIL_VALIDATOR.isValid(user.getEmail())) 
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND + ": Wrong e-mail address"); // Adres e-mail jest nieprawidłowy
	    		
	    
	    	userRepository.save(user);
	        return ResponseEntity.status(HttpStatus.OK).body("Success");
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody User updatedUser)
	{
		User user = userRepository.getById(id);
		
		if(user != null)
		{
			if (!EMAIL_VALIDATOR.isValid(updatedUser.getEmail())) 
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND + ": Wrong e-mail address"); // Adres e-mail jest nieprawidłowy
			user.setEmail(updatedUser.getEmail());
			user.setFirstName(updatedUser.getFirstName());
			user.setLastName(updatedUser.getLastName());
			user.setDateRegistered(updatedUser.getDateRegistered());
			userRepository.update(user);

			return ResponseEntity.status(HttpStatus.OK).body("Success");
		}
		else 
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update unsuccessful. User not found.");
		}
	}
	
	@PatchMapping("update/{id}")
	public ResponseEntity<String> partiallyUpdate(@PathVariable("id") int id, @RequestBody User updatedUser)
	{
		User user = userRepository.getById(id);
		
		if(user != null)
		{
			if(updatedUser.getEmail() != null) 
			{
				if (!EMAIL_VALIDATOR.isValid(updatedUser.getEmail())) 
				{
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong e-mail adjjdress");
				}
				user.setEmail(updatedUser.getEmail());
			}
			if(updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
			if(updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());
			if(updatedUser.getDateRegistered() != null) user.setDateRegistered(updatedUser.getDateRegistered());
			
			userRepository.update(user);
			
			return ResponseEntity.status(HttpStatus.OK).body("Success");
		}
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update unsuccessful. User not found.");
		}
	}
	
	@DeleteMapping("/delete/{userID}")
	public ResponseEntity<String> delete(@PathVariable("userID") int userID)
	{
		User user = userRepository.getById(userID);
		
		if(user != null)
		{
			userRepository.delete(userID);
			return ResponseEntity.status(HttpStatus.OK).body("Success");
		}
		else
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update unsuccessful. User not found.");
		}
	}
	
}