package com.example.java.Mapper;

import com.example.java.Entity.Movie;
import com.example.java.Entity.MovieCountByMonth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// 全部通过
@Mapper
public interface MovieTimeMapper {

    // 查询xx年有哪些电影
    @Select("SELECT m.movie_id, m.name, m.grade, d.month " +
            "FROM movie m JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year}")
    List<Movie> findMoviesByYear(@Param("year") Integer year);

    // 查询xx年xx月有哪些电影
    @Select("SELECT m.movie_id, m.name, m.grade, d.month " +
            "FROM movie m JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} AND d.month = #{month}")
    List<Movie> findMoviesByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

    // 查询xx年xx季度有哪些电影
    @Select("SELECT m.movie_id, m.name, m.grade, d.month " +
            "FROM movie m JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} AND d.season = #{season}")
    List<Movie> findMoviesByYearAndSeason(@Param("year") Integer year, @Param("season") Integer season);

    // 查询xx年发行的电影数量
    @Select("SELECT COUNT(*) FROM movie m JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} GROUP BY d.year")
    Integer countMoviesByYear(@Param("year") Integer year);

    // 查询某年份内每月发行的电影数量
    @Select("SELECT d.year AS year, d.month AS month, COUNT(*) AS count " +
            "FROM movie m " +
            "JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} " +
            "GROUP BY d.year, d.month ")
    List<MovieCountByMonth> countMoviesByYearAndMonth(@Param("year") Integer year);
}
