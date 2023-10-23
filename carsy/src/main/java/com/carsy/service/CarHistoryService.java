package com.carsy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carsy.entity.CarHistory;
import com.carsy.repository.CarHistoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarHistoryService {

    private final CarHistoryRepository carHistoryRepository;

    @Autowired
    public CarHistoryService(CarHistoryRepository carHistoryRepository) {
        this.carHistoryRepository = carHistoryRepository;
    }

    public List<CarHistory> getAllCarHistories() {
        return carHistoryRepository.findAll();
    }

    public Optional<CarHistory> getCarHistoryById(Long serviceId) {
        return carHistoryRepository.findById(serviceId);
    }

    public void addCarHistory(CarHistory carHistory) {
        carHistoryRepository.save(carHistory);
    }

    public void deleteCarHistory(Long serviceId) {
        carHistoryRepository.deleteById(serviceId);
    }
}