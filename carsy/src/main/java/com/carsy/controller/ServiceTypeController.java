package com.carsy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entity.ServiceType;
import com.carsy.repository.ServiceTypeRepository;


@RestController
public class ServiceTypeController 
{
	@Autowired
	ServiceTypeRepository serviceTypeRepository;
	
	@GetMapping("/servicetypes")
	public List<ServiceType> getAll()
	{
		return serviceTypeRepository.getAll();
	}
	
	@GetMapping("/servicetypes/{ServiceTypeID}")
	public ServiceType getById(@PathVariable("ServiceTypeID") int ServiceTypeID)
	{
		return serviceTypeRepository.getById(ServiceTypeID);
	}
	
	
}