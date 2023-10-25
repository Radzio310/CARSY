package com.carsy.entity;

import java.util.Date;

public class CarHistory 
{
	private int serviceID;
	private Date date;
	private String serviceType;
	private int userID;
	
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getUser() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}