package com.ruufilms.Beans;

import java.sql.Timestamp;
import java.util.HashSet;

public class User {
    private String userId;
    private String username;
    private int userType;
    private Timestamp createsAt;

    // Parameterized constructor
    public User(String userId, String username, int userType, Timestamp createsAt) {
        this.userId = userId;
        this.username = username;
        this.userType = userType;
        this.createsAt = createsAt;
    }

    // Default constructor (optional)
    public User() {
    }

    public Timestamp getCreatesAt() {
        return createsAt;
    }

    public void setCreatesAt(Timestamp createsAt) {
        this.createsAt = createsAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
