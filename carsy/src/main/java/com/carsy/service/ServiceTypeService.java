package com.carsy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carsy.entity.ServiceType;
import com.carsy.repository.ServiceTypeRepository;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    @Autowired
    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public ServiceType getServiceTypeById(Long id) {
        return serviceTypeRepository.findById(id).orElse(null);
    }

    public void addServiceType(ServiceType serviceType) {
        serviceTypeRepository.save(serviceType);
    }

    public void deleteServiceType(Long id) {
        serviceTypeRepository.deleteById(id);
    }
}
