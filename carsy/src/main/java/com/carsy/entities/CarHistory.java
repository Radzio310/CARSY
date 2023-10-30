package com.carsy.entities;

import java.util.Date;

public class CarHistory 
{
	private int serviceID;
	private Date date;
	private int serviceTypeID;
	private int userID;
	private int carID;
	
	public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(int serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public int getCarID() {
        return carID;
    }
    
    public void setCarID(int carID) {
        this.carID = carID;
    }
}