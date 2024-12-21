package com.example.java.DTO;

public class MovieWithGradeDTO {
    private String movieName;  // 电影名称
    private Double grade;      // 版本评分

    // Getter 和 Setter 方法
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    // 构造方法（可选）
    public MovieWithGradeDTO() {}

    public MovieWithGradeDTO(String movieName, Double grade) {
        this.movieName = movieName;
        this.grade = grade;
    }

    // 重写 toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "MovieWithGradeDTO{" +
                "movieName='" + movieName + '\'' +
                ", grade=" + grade +
                '}';
    }
}
