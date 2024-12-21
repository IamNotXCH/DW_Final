package com.example.java.Entity;

import java.util.List;

public class Category {
    private String categoryName;  // 类别名称
    private List<String> topMovies;  // 最受欢迎的前20部电影列表

    // 构造函数
    public Category(String categoryName, List<String> topMovies) {
        this.categoryName = categoryName;
        this.topMovies = topMovies;
    }

    // Getter 和 Setter 方法
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getTopMovies() {
        return topMovies;
    }

    public void setTopMovies(List<String> topMovies) {
        this.topMovies = topMovies;
    }

    @Override
    public String toString() {
        return "Category{name='" + categoryName + "', topMovies=" + topMovies + '}';
    }
}
