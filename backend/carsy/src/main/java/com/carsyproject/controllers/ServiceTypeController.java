package com.carsyproject.controllers;

import com.carsyproject.models.ServiceType;
import com.carsyproject.repository.ServiceTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicetype")
@Tag(name = "servicetype", description = "the service type API")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "Add a service type", description = "Add a new service type. Requires MODERATOR or ADMIN role.")
    public ServiceType addServiceType(@RequestBody ServiceType serviceType) {
        return serviceTypeRepository.save(serviceType);
    }
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "Get all service types", description = "Get all service types. Requires MODERATOR or ADMIN role.")
    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/update")
    @Operation(summary = "Update a service type", description = "Update an existing service type. Requires MODERATOR or ADMIN role.")
    public ServiceType updateServiceType(@RequestBody ServiceType serviceType) {
        return serviceTypeRepository.save(serviceType);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a service type", description = "Delete a service type by ID. Requires MODERATOR or ADMIN role.")
    public void deleteServiceType(@PathVariable Long id) {
        serviceTypeRepository.deleteById(id);
    }
}