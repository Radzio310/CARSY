package com.carsy.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entities.ServiceType;
import com.carsy.repositories.ServiceTypeRepository;


@RestController
@RequestMapping("/servicetype")
public class ServiceTypeController 
{
	@Autowired
	ServiceTypeRepository serviceTypeRepository;
	
	@GetMapping("")
	public ResponseEntity<Object> getAll()
	{
		Optional<List<ServiceType>> optionalServices = serviceTypeRepository.getAll();	
		
		if (optionalServices.isPresent()) 
		{
			List<ServiceType> serviceTypesList = optionalServices.get();
			return ResponseEntity.status(HttpStatus.OK).body(serviceTypesList);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
		}
	}
	
	@GetMapping("/{serviceTypeID}")
	public ResponseEntity<Object> getById(@PathVariable("serviceTypeID") int serviceTypeID)
	{
		Optional<ServiceType> optionalService = serviceTypeRepository.getById(serviceTypeID);

		if (optionalService.isPresent()) 
		{
	        ServiceType serviceType = optionalService.get();
	        return ResponseEntity.status(HttpStatus.OK).body(serviceType);
	    }
		else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found.");
	    }
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody ServiceType serviceType)
	{
		if(!validateName(serviceType.getName()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong name.");
		
		if(!validatePrice(serviceType.getPrice()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong price.");
		
		serviceTypeRepository.save(serviceType);
		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}
	
	@PatchMapping("update/{serviceTypeID}")
	public ResponseEntity<String> partiallyUpdate(@PathVariable("serviceTypeID") int serviceTypeID, @RequestBody ServiceType updatedServiceType)
	{
		Optional<ServiceType> optionalServiceType = serviceTypeRepository.getById(serviceTypeID);
		ServiceType serviceType = null;
		
	    if (optionalServiceType.isPresent()) 
	    {
	    	serviceType = optionalServiceType.get();
	    }
	    else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service type not found.");
	    }

		if(!validateName(updatedServiceType.getName()))
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong name.");
		serviceType.setName(updatedServiceType.getName());

		if(!validatePrice(updatedServiceType.getPrice()))
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + ": Wrong price.");
		serviceType.setPrice(updatedServiceType.getPrice());
			
		serviceTypeRepository.update(serviceType);
			
		return ResponseEntity.status(HttpStatus.OK).body("Success.");
	}
	
	@DeleteMapping("/delete/{serviceTypeID}")
	public ResponseEntity<String> delete(@PathVariable("serviceTypeID") int serviceTypeID)
	{
		Optional<ServiceType> optionalServiceType = serviceTypeRepository.getById(serviceTypeID);
		
	    if (optionalServiceType.isPresent()) 
	    {
	    	serviceTypeRepository.delete(serviceTypeID);
			return ResponseEntity.status(HttpStatus.OK).body("Success.");
	    }
	    else 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service type not found.");
	    }
	}
	
	private boolean validateName(String name)
	{
		return name != null && !name.isEmpty() && name.replaceAll("\\s+", "").chars().allMatch(Character::isLetter);
	}
	
	private boolean validatePrice(Double price)
	{
		return price != null && price >= 0;
	}
	
}