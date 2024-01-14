package com.carsyproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "carhistory")
public class CarHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceID;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "service_typeid")
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public CarHistory() {
    }

    public CarHistory(Date date, ServiceType serviceType, Car car) {
        this.date = date;
        this.serviceType = serviceType;
        this.car = car;
    }

    public Long getServiceID() {
        return serviceID;
    }

    public void setServiceID(Long serviceID) {
        this.serviceID = serviceID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Car getCar() { return car; }

    public void setCar(Car car) {
        this.car = car;
    }
}