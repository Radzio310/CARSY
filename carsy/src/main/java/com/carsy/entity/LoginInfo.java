package com.carsy.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LoginInfo {
    @Id
    private Long userID;

    private String passwordHash;
    private String salt;

    @OneToOne
    private User user;

    // Getters and setters
}