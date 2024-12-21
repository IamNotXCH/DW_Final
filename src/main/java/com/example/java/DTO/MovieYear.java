package com.example.java.DTO;


public class MovieYear {
    private Integer time; // 表示年份
    private Integer movieCount; // 表示电影数量

    // Getter 和 Setter 方法
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    // 构造方法（可选）
    public MovieYear() {}

    public MovieYear(Integer time, Integer movieCount) {
        this.time = time;
        this.movieCount = movieCount;
    }

    // 重写 toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "MovieYear{" +
                "time=" + time +
                ", movieCount=" + movieCount +
                '}';
    }
}

