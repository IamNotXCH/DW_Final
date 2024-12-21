package com.example.java.DTO;

public class GenreMovieCountDTO {
    private String genre;
    private int movieCount;

    // Getters and Setters
    public GenreMovieCountDTO(String genre, int movieCount) {
        this.genre = genre;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public int getMovieCount() {
        return movieCount;
    }
    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }
}
