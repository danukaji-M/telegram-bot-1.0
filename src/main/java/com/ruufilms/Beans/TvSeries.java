package com.ruufilms.Beans;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class TvSeries {
    private String groupId;
    private String name;
    private String groupLink;
    private String year;
    private List<String> genres;
    private String createdTime;

    public List<String> getGenres(){
        return genres;
    }

    public void setGenres(List<String> genres){
        this.genres = genres;
    }

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

}
