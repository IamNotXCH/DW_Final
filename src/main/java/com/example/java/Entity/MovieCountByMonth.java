package com.example.java.Entity;

public class MovieCountByMonth {
    private Integer year;  // 年份
    private Integer month; // 月份
    private Long count;    // 当月电影数量

    // Getters and Setters
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
