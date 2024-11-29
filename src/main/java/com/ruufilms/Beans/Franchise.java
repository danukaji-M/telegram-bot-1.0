package com.ruufilms.Beans;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;

public class Franchise {
    private String name;
    private String details;
    private String groupLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }

    public void createFranchise(String name, String details, String groupLink){
        String sql = "INSERT INTO franchise(`name`,`details`,`group_link`) VALUES(?,?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,name,details,groupLink);
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
