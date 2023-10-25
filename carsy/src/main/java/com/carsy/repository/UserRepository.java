package com.carsy.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entity.User;

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
}
