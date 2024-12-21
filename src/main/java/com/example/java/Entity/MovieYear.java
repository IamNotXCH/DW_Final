package com.example.java.Entity;

public class MovieYear {
    private String name;  // 电影名称
    private Double grade; // 电影评分
    private Integer year; // 电影发行年份

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
