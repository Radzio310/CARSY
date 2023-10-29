package com.carsy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.User;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Optional<List<User>> getAll() {
	    try 
	    {
	        List<User> users = jdbcTemplate.query("SELECT UserID, Email, FirstName, LastName, DateRegistered FROM User",
	                BeanPropertyRowMapper.newInstance(User.class));
	        return Optional.ofNullable(users);
	    } 
	    catch (EmptyResultDataAccessException e) 
	    {
	        return Optional.empty();
	    }
	}
	
	public Optional<User> getById(int UserID)
	{
        try 
        {
            User user = jdbcTemplate.queryForObject("SELECT * FROM User WHERE UserID = ?",
                    BeanPropertyRowMapper.newInstance(User.class), UserID);
            return Optional.ofNullable(user);
        } 
        catch (EmptyResultDataAccessException e) 
        {
            return Optional.empty(); 
        }
	}

	public int save(User user) 
	{
		return jdbcTemplate.update("INSERT INTO User(Email, FirstName, LastName, DateRegistered) VALUES(?, ?, ?, ?)",
						user.getEmail(), user.getFirstName(), user.getLastName(), user.getDateRegistered()
						);
	}
	
	public int update(User user) 
	{
		return jdbcTemplate.update("UPDATE user SET Email=?, FirstName=?, LastName=?, DateRegistered=? WHERE UserID=?",
				user.getEmail(), user.getFirstName(), user.getLastName(), user.getDateRegistered(), user.getUserID());
	}
	
	public int delete(int userID)
	{
		return jdbcTemplate.update("DELETE FROM User WHERE UserID=?", userID);
	}
	
	
}
