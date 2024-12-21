package com.example.java.Mapper;

import com.example.java.Entity.MovieRough;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieTypeMapper {

    // 根据电影类型查询电影列表，返回MovieRough
    @Select("SELECT m.movie_id AS movieId, m.name " +
            "FROM movie m " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "WHERE t.type_name = #{typeName}")
    List<MovieRough> findMoviesByType(@Param("typeName") String typeName);
}
