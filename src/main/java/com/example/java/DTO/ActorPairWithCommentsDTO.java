package com.example.java.DTO;

public class ActorPairWithCommentsDTO {
    private String actor1;
    private String actor2;
    private Integer totalComments;

    // Getter 和 Setter 方法
    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }
}

