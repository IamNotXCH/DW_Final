package com.example.java.Mapper;

import com.example.java.DTO.MovieWithGradeDTO;
import com.example.java.DTO.MovieYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieTimeMapper {
    // 按时间统计电影数量
    @Select("SELECT d.year AS time, COUNT(m.movie_id) AS movie_count " +
            "FROM movie m " +
            "JOIN date d ON m.date_id = d.date_id " +
            "GROUP BY d.year " +
            "ORDER BY d.year")
    List<MovieYear> countMoviesByTime();

    // 查询某年某月有多少电影
    @Select("SELECT m.name AS movie_name ,m.grade " +
            "FROM movie m " +
            "JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} AND d.month = #{month}")
    List<MovieWithGradeDTO> countMoviesByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);


    // 查询某年某季度有多少电影
    @Select("SELECT m.name AS movie_name,m.grade " +
            "FROM movie m " +
            "JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} AND d.season = #{season}")
    List<MovieWithGradeDTO> countMoviesByYearAndSeason(@Param("year") Integer year, @Param("season") Integer season);
}
