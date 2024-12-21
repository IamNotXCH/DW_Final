package com.example.java.Entity;

import java.util.List;

public class CategoryTopMovies {
    private String name;
    private String type_name;

    public CategoryTopMovies(String name, String type_name) {
        this.name = name;
        this.type_name = type_name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType_name() {
        return type_name;
    }
    public void setType_name(String type_name) {
        this.type_name = type_name;
    }



}