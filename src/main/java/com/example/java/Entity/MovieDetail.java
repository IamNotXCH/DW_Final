package com.example.java.Entity;

public class MovieDetail {
    private String movieName;
    private int actorCount;
    private int directorCount;
    private int reviewCount;
    private double grade;

    // Getters and Setters
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getDirectorCount() {
        return directorCount;
    }

    public void setDirectorCount(int directorCount) {
        this.directorCount = directorCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
