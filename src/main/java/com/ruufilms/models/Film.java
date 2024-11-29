package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;

public class Film {
    private String filmId;
    private String filmName;
    private String producer;
    private String link;
    private String year;
    private String franchiseId;

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void createFilm(String filmId, String filmName, String producer, String link, String year, String franchise_id){
        String sql = "INSERT INTO films (film_id,film_name,producer,link,year,franchise_id,created_time) VALUES(?,?,?,?,?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql, filmId,filmName,producer,link,year,franchise_id);
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
