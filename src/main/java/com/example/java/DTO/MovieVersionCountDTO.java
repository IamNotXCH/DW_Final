package com.example.java.DTO;

public class MovieVersionCountDTO {
    private double movieGrade;
    private String movieName;
    private int versionCount;

    public double getMovieGrade() {
        return movieGrade;
    }
    public void setMovieGrade(double movieGrade) {
        this.movieGrade = movieGrade;
    }
    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public int getVersionCount() {
        return versionCount;
    }
    public void setVersionCount(int versionCount) {
        this.versionCount = versionCount;
    }
}
