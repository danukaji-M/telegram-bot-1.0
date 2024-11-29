package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;

public class Genres {
    private String genreId;
    private String genreName;

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public void createTvSeriesHasGenres(int genreId, String tvSeriesId){
        String sql = "INSERT INTO (`genres_id`,`tv_series_group_id`) VALUES(?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,genreId,tvSeriesId);
            if(rowsAffected>0){
                System.out.println("Successfully Created");
            }else{
                System.out.println("Something went wrong");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createFilmHasGenres(int genreId, String FilmId){
        String sql = "INSERT INTO (`genres_id`,`films_film_id`) VALUES(?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,genreId,FilmId);
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
