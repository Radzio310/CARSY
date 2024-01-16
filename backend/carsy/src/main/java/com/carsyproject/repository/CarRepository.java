package com.carsyproject.repository;

import com.carsyproject.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByVin(String vin);
}