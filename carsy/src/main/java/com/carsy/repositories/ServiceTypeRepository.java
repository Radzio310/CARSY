package com.carsy.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.ServiceType;


@Repository
public class ServiceTypeRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<ServiceType> getAll()
	{
		return jdbcTemplate.query("SELECT ServiceTypeID, Name, Price FROM ServiceType", 
				BeanPropertyRowMapper.newInstance(ServiceType.class));
	}
	
	public ServiceType getById(int ServiceTypeID)
	{
		return jdbcTemplate.queryForObject("SELECT ServiceTypeID, Name, Price FROM ServiceType WHERE ServiceTypeID = ?", 
				BeanPropertyRowMapper.newInstance(ServiceType.class), ServiceTypeID);
	}

}