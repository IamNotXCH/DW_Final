package com.example.java.Mapper;

import com.example.java.DTO.ActorMoviesDTO;
import com.example.java.DTO.MovieWithGradeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonMapper {
    // 查询某导演共有多少电影
    @Select("SELECT m.name AS movie_name,m.grade " +
            "FROM movie m " +
            "JOIN movie_director md ON m.movie_id = md.movie_id " +
            "JOIN director d ON md.director_id = d.director_id " +
            "WHERE d.director_name = #{directorName}")
    List<MovieWithGradeDTO> countMoviesByDirectorName(@Param("directorName") String directorName);

    // 查询演员共有多少部电影
    @Select("SELECT m.name AS movie_name,m.grade " +
            "FROM actor a " +
            "JOIN movie_actor ma ON a.actor_id = ma.actor_id " +
            "JOIN movie m ON ma.movie_id = m.movie_id " +
            "WHERE a.actor_name = #{actorName} " )
    List<MovieWithGradeDTO> countMoviesByActorName(@Param("actorName") String actorName);


}
