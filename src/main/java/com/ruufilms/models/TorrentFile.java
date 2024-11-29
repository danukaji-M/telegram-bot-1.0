package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;

public class TorrentFile {
    private int id;
    private String torrentName;
    private String userName;
    private String userId;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTorrentName() {
        return torrentName;
    }

    public void setTorrentName(String torrentName) {
        this.torrentName = torrentName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void createTorrentFile(String torrentName, String userId, String userName){
        String sql = "INSERT INTO torrent_files(`torrent_name`,`user_id`,`user_name`) VALUES(?,?,?)";
        try{
           int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,torrentName,userId,userName);
           if(rowsAffected>0){
               System.out.println("Created the torrent File");
           }else{
               System.out.println("Something Went Wrong");
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
