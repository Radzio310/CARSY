package com.carsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carsy.entity.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
	//TODO CRUDY
}
