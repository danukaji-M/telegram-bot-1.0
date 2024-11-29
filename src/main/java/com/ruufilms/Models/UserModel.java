package com.ruufilms.Models;

import com.ruufilms.Beans.User;
import com.ruufilms.config.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
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
            DatabaseConfig.closeConnection();
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
            DatabaseConfig.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Map<String, Object> getAllUsers(){
        try {
            Map<String, Object> userCache = new HashMap<>();

            ResultSet resultSet = DatabaseConfig.executeQuery(
                    "SELECT user.user_id, user.username, user.create_time, user_type.name AS user_type_name " +
                            "FROM user " +
                            "JOIN user_type " +
                            "ON user.user_type_type_id = user_type.type_id"
            );

            // Iterate over each row in the result set
            while (resultSet.next()) {
                User user = new User();
                String userId = resultSet.getString("user_id");
                user.setUserId(userId);
                user.setUsername(resultSet.getString("username"));
                user.setUserType(resultSet.getInt("user_type_type_id"));
                user.setCreatesAt(resultSet.getTimestamp("create_time"));
                userCache.put(userId, user);
            }
            DatabaseConfig.closeConnection();

            return userCache;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
