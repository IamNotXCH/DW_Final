package com.example.java.Mapper;

import com.example.java.DTO.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {
    // 按照电影名称分组，查询电影名称、评分和版本数目
    @Select("SELECT m.name AS movie_name, m.grade AS movie_grade, COUNT(DISTINCT mv.version_id) AS version_count " +
            "FROM movie m " +
            "JOIN movie_version mv ON m.movie_id = mv.movie_id " +
            "WHERE m.name LIKE CONCAT('%', #{movieName}, '%') " +  // 模糊匹配电影名称
            "GROUP BY m.name, m.grade")
    List<MovieVersionCountDTO> countMoviesGroupedByNameWithVersionCount(@Param("movieName") String movieName);




    // 查找某类别的所有电影
    @Select("SELECT m.name AS movie_name, m.grade  " +
            "FROM dsmdatabase.movie m " +
            "JOIN dsmdatabase.movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN dsmdatabase.type t ON mt.type_id = t.type_id " +
            "WHERE t.type_name = #{typeName}")
    List<MovieWithGradeDTO> findMoviesByType(@Param("typeName") String typeName);


    // 查询评分高于 4.5 的电影
    @Select("SELECT m.name AS MovieName, m.grade AS Grade " +
            "FROM dsmdatabase.movie m " +
            "WHERE m.grade > 4.5 "+
            "LIMIT #{limit}")
    List<MovieWithGradeDTO> findMoviesWithHighGrade(@Param("score") double score,@Param("limit") int limit);


    // 查询用户评价中有正面评价的电影名称
    @Select("SELECT DISTINCT m.name AS MovieName " +
            "FROM dsmdatabase.movie m " +
            "WHERE m.good_review_count>0 "+
            "LIMIT #{limit}")
    List<String> findMoviesWithPositiveReviews(@Param("limit") int limit);


    @Select("SELECT m.grade AS movie_grade, m.name AS MovieName, COUNT(mv.version_id) AS VersionCount " +
            "FROM movie m " +
            "JOIN movie_version mv ON m.movie_id = mv.movie_id " +
            "GROUP BY m.movie_id " +
            "HAVING VersionCount > 1 " +
            "LIMIT #{limit}")
    List<MovieVersionCountDTO> findMoviesWithMultipleVersions(@Param("limit") int limit);



}
