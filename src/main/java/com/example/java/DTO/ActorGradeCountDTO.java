package com.example.java.DTO;

public class ActorGradeCountDTO {
    String ActorPair;
    Double AverageScore;
    Integer MovieCount;

    public String getActorPair() {
        return ActorPair;
    }
    public void setActorPair(String ActorPair) {
        this.ActorPair = ActorPair;
    }
    public Double getAverageScore() {
        return AverageScore;
    }
    public void setAverageScore(Double AverageScore) {
        this.AverageScore = AverageScore;
    }
    public Integer getMovieCount() {
        return MovieCount;
    }
    public void setMovieCount(Integer MovieCount) {
        this.MovieCount = MovieCount;
    }


}
