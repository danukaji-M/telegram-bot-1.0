package com.ruufilms.beans;

import java.io.Serializable;

public class BookBean implements Serializable {
    private String name;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String book_id;

}
