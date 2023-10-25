package com.carsy.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class LoginInfo
{
	private int userID;
	private String passwordHash;
	private String salt;
	
	@OneToOne
    @JoinColumn(name = "userID")
    private User user;
	
	public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
	
}