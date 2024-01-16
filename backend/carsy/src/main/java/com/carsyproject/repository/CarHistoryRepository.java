package com.carsyproject.repository;

import com.carsyproject.models.Car;
import com.carsyproject.models.CarHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarHistoryRepository extends JpaRepository<CarHistory, Long> {
    List<CarHistory> findByCar(Car car);
    List<CarHistory> findByCarUserId(Long userId);
    Optional<CarHistory> findByDate(Date date);

}