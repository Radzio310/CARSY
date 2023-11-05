package com.carsy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.carsy.entities.LoginInfo;
import com.carsy.repositories.LoginInfoRepository;


@RestController
public class LoginInfoController 
{
	@Autowired
	LoginInfoRepository loginInfoRepository;
	
	@GetMapping("/logininfos")
	public List<LoginInfo> getAll()
	{
		return loginInfoRepository.getAll();
	}
	
	@GetMapping("/logininfos/{UserID}")
	public LoginInfo getById(@PathVariable("UserID") int UserID)
	{
		return loginInfoRepository.getById(UserID);
	}
}