package com.carsy.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ServiceType
{
	@NotNull(message = "ID is empty")
	@Positive(message = "ID must be positive")
	private int serviceTypeID;
	@NotBlank(message = "Nazwa nie może być pusta")
    private String name;
	@Positive(message = "Price must be positive")
    private double price;

    public int getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(int serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
	
    }
}