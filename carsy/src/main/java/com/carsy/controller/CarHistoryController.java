package com.carsy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entity.CarHistory;
import com.carsy.repository.CarHistoryRepository;


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