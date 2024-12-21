package com.example.java.DTO;

public class MovieStatisticsDTO {

    private int movieCount;  // 哈利波特系列电影数量
    private int versionCount;  // 版本数量
    private int webCount;  // 网页数量

    public int getMovieCount() {
        return movieCount;
    }
    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }
    public int getVersionCount() {
        return versionCount;
    }
    public void setVersionCount(int versionCount) {
        this.versionCount = versionCount;
    }
    public int getWebCount() {
        return webCount;
    }
    public void setWebCount(int webCount) {
        this.webCount = webCount;
    }
}

