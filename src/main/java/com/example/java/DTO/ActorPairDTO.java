package com.example.java.DTO;

public class ActorPairDTO {
    private String actor1Name;  // 第一个演员的名字
    private String actor2Name;  // 第二个演员的名字
    private Integer movieCount; // 合作的电影数量

    // Getter 和 Setter 方法
    public String getActor1Name() {
        return actor1Name;
    }

    public void setActor1Name(String actor1Name) {
        this.actor1Name = actor1Name;
    }

    public String getActor2Name() {
        return actor2Name;
    }

    public void setActor2Name(String actor2Name) {
        this.actor2Name = actor2Name;
    }

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    // 构造方法（可选）
    public ActorPairDTO() {}

    public ActorPairDTO(String actor1Name, String actor2Name, Integer movieCount) {
        this.actor1Name = actor1Name;
        this.actor2Name = actor2Name;
        this.movieCount = movieCount;
    }

    // 重写 toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "ActorPairDTO{" +
                "actor1Name='" + actor1Name + '\'' +
                ", actor2Name='" + actor2Name + '\'' +
                ", movieCount=" + movieCount +
                '}';
    }
}
