package com.ruufilms.Beans;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;

public class Group {
    private String groupId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void createGroup(String groupId, String name){
        String sql = "INSERT INTO group(`group_id`,`name`) VALUES (?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,groupId,name);
            if(rowsAffected>0){
                System.out.println("Successfully Created");
            }else{
                System.out.println("Something went wrong");
            }
            DatabaseConfig.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
