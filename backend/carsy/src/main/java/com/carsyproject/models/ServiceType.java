package com.carsyproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "servicetype")
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceTypeID;

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    public ServiceType() {
    }

    public ServiceType(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Long getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(Long serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}