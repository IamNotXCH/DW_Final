package com.example.java.Mapper;

import com.example.java.Entity.MovieYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// 通过
@Mapper
public interface MovieReviewMapper {

    // 查询评分大于等于某个分数的电影，返回电影名称、评分和发行年份
    @Select("SELECT md.name, md.grade, d.year " +
            "FROM movie md " +
            "JOIN date d ON md.date_id = d.date_id " +
            "WHERE md.grade >= #{score} " +
            "ORDER BY md.grade DESC " +  // 确保在 ORDER BY 和下一部分之间有空格
            "LIMIT 100")
    List<MovieYear> findMoviesByScore(@Param("score") double score);

    @Select("SELECT md.name, md.grade, d.year " +
            "FROM movie md " +
            "JOIN date d ON md.date_id = d.date_id " +
            "WHERE md.good_review_count > 0 " +
            "ORDER BY md.grade DESC " +  // 按评分降序排序
            "LIMIT 100")
    List<MovieYear> findMoviesWithPositiveReview();

    @Select("SELECT md.name, md.grade, d.year " +
            "FROM movie md " +
            "JOIN date d ON md.date_id = d.date_id " +
            "WHERE md.bad_review_count = 0 " +
            "ORDER BY md.grade DESC " +  // 按评分降序排序
            "LIMIT 100")
    List<MovieYear> findMoviesWithAllPositiveReview();

    // 查询至少有一条负面评价的电影，返回电影名称、评分和发行年份
    @Select("SELECT md.name, md.grade, d.year " +
            "FROM movie md " +
            "JOIN date d ON md.date_id = d.date_id " +
            "WHERE md.bad_review_count > 0 " +
            "ORDER BY md.grade DESC " +  // 按评分降序排序
            "LIMIT 100")
    List<MovieYear> findMoviesWithNegativeReview();

    // 查询好评数最多的电影，返回电影名称、评分和发行年份
    @Select("SELECT md.name, md.grade, d.year " +
            "FROM movie md " +
            "JOIN date d ON md.date_id = d.date_id " +
            "ORDER BY md.good_review_count DESC LIMIT 1")
    MovieYear findMovieWithMostPositiveReviews();


}
