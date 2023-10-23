package com.carsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carsy.entity.ServiceType;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> 
{
	//TODO CRUDY
}