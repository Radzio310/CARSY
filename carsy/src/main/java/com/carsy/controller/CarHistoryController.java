package com.carsy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.carsy.entity.CarHistory;
import com.carsy.service.CarHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/car-history")
public class CarHistoryController {

    private final CarHistoryService carHistoryService;

    @Autowired
    public CarHistoryController(CarHistoryService carHistoryService) {
        this.carHistoryService = carHistoryService;
    }

    @GetMapping
    public List<CarHistory> getAllCarHistories() {
        return carHistoryService.getAllCarHistories();
    }

    @GetMapping("/{serviceId}")
    public CarHistory getCarHistoryById(@PathVariable Long serviceId) {
        return carHistoryService.getCarHistoryById(serviceId).orElse(null);
    }

    @PostMapping
    public void addCarHistory(@RequestBody CarHistory carHistory) {
        carHistoryService.addCarHistory(carHistory);
    }

    @DeleteMapping("/{serviceId}")
    public void deleteCarHistory(@PathVariable Long serviceId) {
        carHistoryService.deleteCarHistory(serviceId);
    }
}