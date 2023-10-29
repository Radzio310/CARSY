package com.carsy.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;

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
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Object> getAll()
	{
		Optional<List<User>> optionalUsers = userRepository.getAll();	
		
		if (optionalUsers.isPresent()) 
		{
			List<User> userList = optionalUsers.get();
			return ResponseEntity.status(HttpStatus.OK).body(userList);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
	
	@GetMapping("/{UserID}")
	public ResponseEntity<Object> getById(@PathVariable("UserID") int UserID)
	{
		Optional<User> optionalUser = userRepository.getById(UserID);

		if (optionalUser.isPresent()) 
		{
	        User user = optionalUser.get();
	        return ResponseEntity.status(HttpStatus.OK).body(user);
	    }
		else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    }
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody User user) 
	{
	    if (!EMAIL_VALIDATOR.isValid(user.getEmail())) 
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong e-mail address."); // Adres e-mail jest nieprawidłowy
	    
	    if(!validateFirstName(user.getFirstName()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong first name.");
	    
	    if(!validateLastName(user.getLastName()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong last name.");
	    
//	    if(!validateDate(user.getDateRegistered()))
//    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong date.");
	    
	    userRepository.save(user);
	    return ResponseEntity.status(HttpStatus.OK).body("Success.");
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody User updatedUser)
	{
		Optional<User> optionalUser = userRepository.getById(id);
		User user = null;
		
	    if (optionalUser.isPresent()) 
	    {
	        user = optionalUser.get();
	    }
	    else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    }
		
		if (!EMAIL_VALIDATOR.isValid(updatedUser.getEmail())) 
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong e-mail address."); // Adres e-mail jest nieprawidłowy
		user.setEmail(updatedUser.getEmail());
		
		if(!validateFirstName(updatedUser.getFirstName()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong first name.");
		user.setFirstName(updatedUser.getFirstName());
		
		if(!validateLastName(user.getLastName()))
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong last name.");
		user.setLastName(updatedUser.getLastName());
		
		
		user.setDateRegistered(updatedUser.getDateRegistered());
		userRepository.update(user);

		return ResponseEntity.status(HttpStatus.OK).body("Success.");
	}
	
	@PatchMapping("update/{id}")
	public ResponseEntity<String> partiallyUpdate(@PathVariable("id") int id, @RequestBody User updatedUser)
	{
		Optional<User> optionalUser = userRepository.getById(id);
		User user = null;
		
	    if (optionalUser.isPresent()) 
	    {
	        user = optionalUser.get();
	    }
	    else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    }

		if(updatedUser.getEmail() != null) 
		{
			if (!EMAIL_VALIDATOR.isValid(updatedUser.getEmail())) 
			{
		    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong e-mail address.");
			}
			user.setEmail(updatedUser.getEmail());
		}
		if(updatedUser.getFirstName() != null) 
		{
			if(!validateFirstName(updatedUser.getFirstName()))
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong first name.");
			user.setFirstName(updatedUser.getFirstName());
		}
		if(updatedUser.getLastName() != null) 
		{
			if(!validateLastName(user.getLastName()))
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong last name.");
			user.setLastName(updatedUser.getLastName());
		}
		if(updatedUser.getDateRegistered() != null) 
			user.setDateRegistered(updatedUser.getDateRegistered());
			
		userRepository.update(user);
			
		return ResponseEntity.status(HttpStatus.OK).body("Success.");
	}

	@DeleteMapping("/delete/{userID}")
	public ResponseEntity<String> delete(@PathVariable("userID") int userID)
	{
		Optional<User> optionalUser = userRepository.getById(userID);
		
	    if (optionalUser.isPresent()) 
	    {
	    	userRepository.delete(userID);
			return ResponseEntity.status(HttpStatus.OK).body("Success.");
	    }
	    else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    }
	}
	
	private boolean validateFirstName(String firstName)
	{
		return firstName.matches("^[A-ZĄĆĘŁŃÓŚŻŹ][a-zęąćłńóśżź]+$") || firstName.matches("^[A-ZĄĆĘŁŃÓŚŻŹ][a-zęąćłńóśżź]+\\s[A-ĄĆĘŁŃÓŚŻŹ][a-zęąćłńóśżź]+$");
	}
	
	private boolean validateLastName(String lastName)
	{
		return lastName.matches("^[A-ZĄĆĘŁŃÓŚŻŹa-zęąćłńóśżź]+([ '-][A-ZĄĆĘŁŃÓŚŻŹa-zęąćłńóśżź]+)*$");  
	}
	
//	private boolean validateDate(Date date) 
//	{
//		final String DATE_FORMAT = "yyyy-MM-dd";
//		
//        if (date == null) 
//        {
//            return false;
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//        sdf.setLenient(false);
//
//        try 
//        {
//            // Sprawdź, czy sparsowana data jest taka sama jak pierwotna data
//            return date.equals(sdf.parse(sdf.format(date)));
//        } 
//        catch (Exception e) 
//        {
//            return false;
//        }
//    }
}