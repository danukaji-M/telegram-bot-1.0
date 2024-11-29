package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;
import java.sql.Timestamp;

public class User {
    private String userId;
    private String username;
    private int userType;


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

    public void createUser(String userId, String username, int userType){
        String sql = "INSERT INTO user(user_id,username,user_type_type_id,create_time) VALUES(?,?,?,?)";
        try{
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,userId, username, userType, currentTime);
            if(rowsAffected >0 ){
                System.out.println("User Created Successfully !");
            }else{
                System.out.println("Failed to create user");
            }
        }catch(SQLException e){
           e.printStackTrace();
        }
    }
    public void createUserHasGroup(String userId, String groupId){
        String sql = "INSERT INTO user_has_group(user_user_id,groups_group_id) VALUES(?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql, userId, groupId);
            if(rowsAffected >0 ){
                System.out.println("User Created Successfully !");
            }else{
                System.out.println("Failed to create user");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
