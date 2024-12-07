package com.ruufilms.Beans;

import java.util.List;

public class Film {
    private int id;
    private String filmName;
    private String fileId;
    private String year;
    private String releasedYear;
    private float imdbRate;
    private String director;
    private List<String> genres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    public float getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(float imdbRate) {
        this.imdbRate = imdbRate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setGenres(List<String> genres){
        this.genres = genres;
    }

    public List<String> getGenres(){
        return this.genres;
    }
}
