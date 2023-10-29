package com.carsy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.carsy.entities.ServiceType;

@Repository
public class ServiceTypeRepository 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Optional<List<ServiceType>> getAll() 
	{
	    try 
	    {
	        List<ServiceType> serviceTypes = jdbcTemplate.query("SELECT ServiceTypeID, Name, Price FROM ServiceType",
	                BeanPropertyRowMapper.newInstance(ServiceType.class));
	        return Optional.ofNullable(serviceTypes);
	    } 
	    catch (EmptyResultDataAccessException e) 
	    {
	        return Optional.empty();
	    }
	}
	
	public Optional<ServiceType> getById(int serviceTypeId) 
	{
	    try 
	    {
	        ServiceType serviceType = jdbcTemplate.queryForObject(
	                "SELECT ServiceTypeID, Name, Price FROM ServiceType WHERE ServiceTypeID = ?",
	                BeanPropertyRowMapper.newInstance(ServiceType.class), serviceTypeId);
	        return Optional.ofNullable(serviceType);
	    } 
	    catch (EmptyResultDataAccessException e) 
	    {
	        return Optional.empty();
	    }
	}
	
	public int save(ServiceType serviceType) 
	{
		return jdbcTemplate.update("INSERT INTO ServiceType(Name, Price) VALUES(?, ?)", 
				serviceType.getName(), serviceType.getPrice());
	}
	
	public int update(ServiceType serviceType) 
	{
		return jdbcTemplate.update("UPDATE serviceType SET Name=?, Price=? WHERE ServiceTypeID=?",
				serviceType.getName(), serviceType.getPrice(), serviceType.getServiceTypeID());
	}

	public int delete(int serviceTypeID)
	{
		return jdbcTemplate.update("DELETE FROM ServiceType WHERE ServiceTypeID=?", serviceTypeID);
	}
	

}