package com.example.java.Mapper;

import com.example.java.Entity.ActorDirectorCooperation;
import com.example.java.Entity.MovieRough;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// 全部通过
@Mapper
public interface PersonMapper {

    // 查询导演一共导演过哪些电影
    @Select("SELECT m.movie_id, m.name FROM movie m " +
            "JOIN movie_director md ON m.movie_id = md.movie_id " +
            "JOIN director d ON md.director_id = d.director_id " +
            "WHERE d.director_name = #{directorName}")
    List<MovieRough> findMoviesByDirector(@Param("directorName") String directorName);

    // 查询演员参演过哪些电影
    @Select("SELECT m.movie_id, m.name FROM movie m " +
            "JOIN movie_actor ma ON m.movie_id = ma.movie_id " +
            "JOIN actor a ON ma.actor_id = a.actor_id " +
            "WHERE a.actor_name = #{actorName}")
    List<MovieRough> findMoviesByActor(@Param("actorName") String actorName);

    // 查询某位导演合作过的演员并按合作次数排序
    // 优化 改成直接建立演员导演合作表
    @Select("SELECT a.actor_name, dac.cooperation_count  " +
            "FROM director_actor_cooperation dac " +
            "JOIN director d ON dac.director_id = d.director_id " +
            "JOIN actor a ON dac.actor_id = a.actor_id " +
            "WHERE d.director_name = #{directorName} " +
            "ORDER BY dac.cooperation_count DESC")
    List<ActorDirectorCooperation> findCooperatingActorsByDirector(@Param("directorName") String directorName);






}
