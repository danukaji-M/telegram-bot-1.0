package com.ruufilms.models;

public class Film {
    private String id;
    private String name;
    private String category;

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void category(String category){
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }

    @Override
    public String toString(){
        return "Film{id='"+id+"', name='"+name+"', category='"+category+"'}";
    }
}
