package com.ruufilms.Beans;

import java.sql.Timestamp;
import java.util.List;

public class User {
    private String id;
    private String username;
    private Timestamp create_time;
    private int user_type_type_id;
    private List<String> groups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }


    public int getUser_type_type_id() {
        return user_type_type_id;
    }

    public void setUser_type_type_id(int user_type_type_id) {
        this.user_type_type_id = user_type_type_id;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
