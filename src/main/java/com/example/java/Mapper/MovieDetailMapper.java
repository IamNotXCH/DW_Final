package com.example.java.Mapper;

import com.example.java.Entity.MovieDetail;
import com.example.java.Entity.MovieVersion;
import com.example.java.Entity.MovieVersionAndType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// 通过

@Mapper
public interface MovieDetailMapper {

    // 根据电影名查找电影的演员数量、导演数量、评论数量、评分
    @Select("SELECT m.name AS movieName, " +
            "COUNT(DISTINCT ma.actor_id) AS actorCount, " +
            "COUNT(DISTINCT md.director_id) AS directorCount, " +
            "m.comment_count AS reviewCount, " +
            "m.grade " +
            "FROM movie m " +
            "LEFT JOIN movie_actor ma ON m.movie_id = ma.movie_id " +
            "LEFT JOIN movie_director md ON m.movie_id = md.movie_id " +
            "WHERE m.name = #{movieName}" +
            "GROUP BY m.movie_id")
    MovieDetail findMovieDetailsByName(@Param("movieName") String movieName);

    // 根据电影名查找电影的所有演员名单
    @Select("SELECT a.actor_name " +
            "FROM movie m " +
            "JOIN movie_actor ma ON m.movie_id = ma.movie_id " +
            "JOIN actor a ON ma.actor_id = a.actor_id " +
            "WHERE m.name = #{movieName}")
    List<String> findActorsByMovieName(@Param("movieName") String movieName);


    // 根据电影名查找电影的所有导演名单
    @Select("SELECT d.director_name " +
            "FROM movie m " +
            "JOIN movie_director md ON m.movie_id = md.movie_id " +
            "JOIN director d ON md.director_id = d.director_id " +
            "WHERE m.name = #{movieName}")
    List<String> findDirectorsByMovieName(@Param("movieName") String movieName);


    // 查询版本信息的方法
    @Select("""
            SELECT
                mv.version_id,
                mvd.format
            FROM
                movie m
            JOIN
                movie_version mv ON m.movie_id = mv.movie_id
            JOIN
                movie_version_detail mvd ON mv.version_id = mvd.version_id
            WHERE
                m.name = #{movieName};  -- 请替换为实际的电影名称
    """)
    List<MovieVersion> findVersionsByMovieName(@Param("movieName") String movieName);

    @Select("SELECT DISTINCT t.type_name AS typeName " +
            "FROM movie_type mt " +
            "LEFT JOIN type t ON mt.type_id = t.type_id " +
            "LEFT JOIN movie m ON mt.movie_id = m.movie_id " +
            "WHERE m.name = #{movieName}")
    List<String> findTypesByMovieName(@Param("movieName") String movieName);





}
