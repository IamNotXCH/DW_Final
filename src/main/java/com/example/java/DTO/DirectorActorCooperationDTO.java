package com.example.java.DTO;

public class DirectorActorCooperationDTO {
    private String directorName;  // 导演的名字
    private String actorName;     // 演员的名字
    private Integer movieCount;   // 合作的电影数量

    // Getter 和 Setter 方法
    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

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

    // 构造方法（可选）
    public DirectorActorCooperationDTO() {}

    public DirectorActorCooperationDTO(String directorName, String actorName, Integer movieCount) {
        this.directorName = directorName;
        this.actorName = actorName;
        this.movieCount = movieCount;
    }

    // 重写 toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "DirectorActorCooperationDTO{" +
                "directorName='" + directorName + '\'' +
                ", actorName='" + actorName + '\'' +
                ", movieCount=" + movieCount +
                '}';
    }
}
