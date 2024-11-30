package com.ruufilms.Models;

import com.ruufilms.Beans.Group;
import com.ruufilms.config.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GroupModel {
    public void createGroup(String groupId, String name, String groupLink){
        String sql = "INSERT INTO `groups`(`group_id`,`name`,`group_link`) VALUES (?,?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,groupId,name,groupLink);
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

    public HashMap<String, Group> getAllGroupData(){
        HashMap <String, Group> groupHashMap = new HashMap<>();
        String sql = "SELECT * FROM `groups`";
        try{
            ResultSet resultSet = DatabaseConfig.executeQuery(sql);
            while(resultSet.next()){
                Group group = new Group();
                String groupId = resultSet.getString("group_id");
                String name = resultSet.getString("name");
                String group_link = resultSet.getString("group_link");

                group.setGroupLink(group_link);
                group.setName(name);
                group.setGroupId(groupId);

                groupHashMap.put(groupId,group);
            }

            DatabaseConfig.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return groupHashMap;
    }
}
