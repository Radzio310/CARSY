package com.carsy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.carsy.entity.ServiceType;
import com.carsy.service.ServiceTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @Autowired
    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeService.getAllServiceTypes();
    }

    @GetMapping("/{id}")
    public ServiceType getServiceTypeById(@PathVariable Long id) {
        return serviceTypeService.getServiceTypeById(id);
    }

    @PostMapping
    public void addServiceType(@RequestBody ServiceType serviceType) {
        serviceTypeService.addServiceType(serviceType);
    }

    @DeleteMapping("/{id}")
    public void deleteServiceType(@PathVariable Long id) {
        serviceTypeService.deleteServiceType(id);
    }
}