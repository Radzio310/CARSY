package com.carsy.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entity.LoginInfo;

@Repository
public class LoginInfoRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<LoginInfo> getAll()
	{
		return jdbcTemplate.query("SELECT UserID, PasswordHash, Salt FROM LoginInfo", 
				BeanPropertyRowMapper.newInstance(LoginInfo.class));
	}
	
	public LoginInfo getById(int UserID)
	{
		return jdbcTemplate.queryForObject("SELECT UserID, PasswordHash, Salt FROM LoginInfo WHERE UserID = ?", 
				BeanPropertyRowMapper.newInstance(LoginInfo.class), UserID);
	}
	
}