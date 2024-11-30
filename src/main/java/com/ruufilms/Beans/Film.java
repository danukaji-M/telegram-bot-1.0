package com.ruufilms.Beans;

import com.ruufilms.config.DatabaseConfig;

import java.util.HashSet;
import java.util.Set;

public class Film {
    private String filmId;
    private String filmName;
    private String producer;
    private String link;
    private String year;
    private int franchiseId;
    private Set<String> genres;  // Using a Set to avoid duplicate genres

    public Film(String filmId, String filmName, String producer, String link, String year, int franchiseId) {
        this.filmId = filmId;
        this.filmName = filmName;
        this.producer = producer;
        this.link = link;
        this.year = year;
        this.franchiseId = franchiseId;
        this.genres = new HashSet<>();
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }

    // Getters and setters
    public String getFilmId() {
        return filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getProducer() {
        return producer;
    }

    public String getLink() {
        return link;
    }

    public String getYear() {
        return year;
    }

    public int getFranchiseId() {
        return franchiseId;
    }

    public Set<String> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmId='" + filmId + '\'' +
                ", filmName='" + filmName + '\'' +
                ", producer='" + producer + '\'' +
                ", link='" + link + '\'' +
                ", year='" + year + '\'' +
                ", franchiseId=" + franchiseId +
                ", genres=" + genres +
                '}';
    }
}

//    public void createFilm(String filmId, String filmName, String producer, String link, String year, String franchise_id){
//        String sql = "INSERT INTO films (film_id,film_name,producer,link,year,franchise_id,created_time) VALUES(?,?,?,?,?,?)";
//        try{
//            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql, filmId,filmName,producer,link,year,franchise_id);
//            if(rowsAffected >0 ){
//                System.out.println("User Created Successfully !");
//            }else{
//                System.out.println("Failed to create user");
//            }
//            DatabaseConfig.closeConnection();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
//
//}
