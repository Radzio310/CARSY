package com.carsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carsy.entity.CarHistory;

public interface CarHistoryRepository extends JpaRepository<CarHistory, Long> 
{
	//TODO CRUDY
}