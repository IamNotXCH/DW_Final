package com.example.java.DTO;

import java.util.List;

public class ActorMoviesDTO {
    private String actorName; // 演员名称
    private Integer movieCount; // 电影数量
    private List<String> topMovies; // 前五部电影

    // Getter 和 Setter 方法
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    public List<String> getTopMovies() {
        return topMovies;
    }

    public void setTopMovies(List<String> topMovies) {
        this.topMovies = topMovies;
    }

    // 构造方法（可选）
    public ActorMoviesDTO() {}

    public ActorMoviesDTO(String actorName, Integer movieCount, List<String> topMovies) {
        this.actorName = actorName;
        this.movieCount = movieCount;
        this.topMovies = topMovies;
    }

    // 重写 toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "ActorMoviesDTO{" +
                "actorName='" + actorName + '\'' +
                ", movieCount=" + movieCount +
                ", topMovies=" + topMovies +
                '}';
    }
}

