package com.carsy.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController 
{
	private final AuthenticationService service;
	
	@PostMapping("/api/v1/auth/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
	{
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/api/v1/auth/authenticate")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request)
	{
		return ResponseEntity.ok(service.authenticate(request));
	}
	
}
