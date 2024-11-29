package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;
import java.sql.Timestamp;

public class TvSeries {
    private String groupId;
    private String name;
    private String groupLink;
    private String year;
    private String createdTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void createTvSeries(String groupId, String name, String groupLink, String year){
        String sql = "INSER INTO tv_series(`group_id`,`name`,`group_link`,`year`,`created_time`) VALUES(?,?,?,?,?)";
        try{
            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql, groupId,name,groupLink,year,timeStamp);
            if(rowsAffected>0){
                System.out.println("Successfully Created");
            }else{
                System.out.println("Something went wrong");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
