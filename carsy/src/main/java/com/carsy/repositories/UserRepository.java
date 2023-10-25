package com.carsy.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.User;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<User> getAll()
	{
		return jdbcTemplate.query("SELECT UserID, Email, FirstName, LastName, DateRegistered FROM User", 
				BeanPropertyRowMapper.newInstance(User.class));
	}
	
	public User getById(int UserID)
	{
		return jdbcTemplate.queryForObject("SELECT UserID, Email, FirstName, LastName, DateRegistered FROM User WHERE UserID = ?", 
				BeanPropertyRowMapper.newInstance(User.class), UserID);
	}

	public int save(List<User> users) {
		users.forEach(user -> jdbcTemplate
				.update("INSERT INTO User(Email, FirstName, LastName, DateRegistered) VALUES(?, ?, ?, ?)",
						user.getEmail(), user.getFirstName(), user.getLastName(), user.getDateRegistered()
						));

		return 1;
	}
}
