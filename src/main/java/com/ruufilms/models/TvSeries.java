package com.ruufilms.models;

public class TvSeries {
    private String id;
    private String name;
    private String category;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString(){
        return "TvSeries{id='"+id+"', name='"+name+"', category='"+category+"'}";
    }
}
