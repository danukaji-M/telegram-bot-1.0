package com.ruufilms.Beans;

import com.ruufilms.config.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String username;
    private int userType;

    public Timestamp getCreatesAt() {
        return createsAt;
    }

    public void setCreatesAt(Timestamp createsAt) {
        this.createsAt = createsAt;
    }

    private Timestamp createsAt;

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
