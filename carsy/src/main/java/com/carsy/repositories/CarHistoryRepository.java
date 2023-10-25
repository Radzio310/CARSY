package com.carsy.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.CarHistory;


@Repository
public class CarHistoryRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<CarHistory> getAll() 
	{
		return jdbcTemplate.query("SELECT ServiceID, Date, ServiceType, UserID FROM CarHistory", 
				BeanPropertyRowMapper.newInstance(CarHistory.class));
	}
		
}