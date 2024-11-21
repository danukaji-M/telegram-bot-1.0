package com.ruufilms.beans;

import java.io.Serializable;

public class FilmBean implements Serializable {
    public String getFilm_name() {
        return film_name;
    }

    public void setFilm_name(String film_name) {
        this.film_name = film_name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilm_id() {
        return film_id;
    }

    public void setFilm_id(String film_id) {
        this.film_id = film_id;
    }

    public String getUniverse() {
        return Universe;
    }

    public void setUniverse(String universe) {
        Universe = universe;
    }

    private String film_name;
    private String producer;
    private String category;
    private String film_id;
    private String Universe;
}
