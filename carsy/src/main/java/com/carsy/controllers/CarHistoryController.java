package com.carsy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entities.CarHistory;
import com.carsy.repositories.CarHistoryRepository;


@RestController
public class CarHistoryController 
{
	@Autowired
	CarHistoryRepository carHistoryRepository;
	
	@GetMapping("/carhistory")
	public List<CarHistory> getAll()
	{
		return carHistoryRepository.getAll();
	}
	
	
	
}