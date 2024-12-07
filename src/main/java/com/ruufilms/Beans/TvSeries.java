package com.ruufilms.Beans;

import java.util.List;

public class TvSeries {
    private int id;
    private String groupId;
    private String groupLink;
    private String released;
    private String addedYear;
    private float imdb;
    private String director;
    private List<String> genres;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getAddedYear() {
        return addedYear;
    }

    public void setAddedYear(String addedYear) {
        this.addedYear = addedYear;
    }

    public float getImdb() {
        return imdb;
    }

    public void setImdb(float imdb) {
        this.imdb = imdb;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
