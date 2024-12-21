package com.example.java.Mapper;

import com.example.java.DTO.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelationMapper {
    // 查询常合作的演员组合和他们的合作电影数量
    @Select("SELECT " +
            "  a1.actor_name AS actor1_name, " +  // 返回演员1的名字
            "  a2.actor_name AS actor2_name, " +  // 返回演员2的名字
            "  aac.cooperation_count AS movie_count " + // 汇总所有类型的合作次数
            "FROM dsmdatabase.actor_actor_cooperation aac " +
            "JOIN dsmdatabase.actor a1 ON aac.actor_id_1 = a1.actor_id " +
            "JOIN dsmdatabase.actor a2 ON aac.actor_id_2 = a2.actor_id " +
            "ORDER BY aac.cooperation_count DESC "+
            "LIMIT #{limit}")
    List<ActorPairDTO> findMostCollaborativeActorPairs(@Param("limit") int limit);




    // 查询经常一起合作的导演和演员，以及他们合作的电影数量
    @Select("SELECT " +
            "  d.director_name AS director_name, " +  // 返回导演的名字
            "  a.actor_name AS actor_name, " +        // 返回演员的名字
            "  dac.cooperation_count AS movie_count " +
            "FROM dsmdatabase.actor_director_cooperation dac " +
            "JOIN dsmdatabase.director d ON dac.director_id = d.director_id " +
            "JOIN dsmdatabase.actor a ON dac.actor_id = a.actor_id " +
            "ORDER BY dac.cooperation_count DESC "+
            "LIMIT #{limit}")
    List<DirectorActorCooperationDTO> findMostCollaborativeDirectorsAndActors(@Param("limit") int limit);



    // 查找最受欢迎的演员组合
    @Select("SELECT " +
            "a1.actor_name AS Actor1, " +
            "a2.actor_name AS Actor2, " +
            "SUM(CASE " +
            "WHEN m.comment_count IS NULL THEN 0 " +
            "ELSE m.comment_count " +
            "END) AS TotalComments " +
            "FROM movie m " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "JOIN movie_actor ma1 ON m.movie_id = ma1.movie_id " +
            "JOIN actor a1 ON ma1.actor_id = a1.actor_id " +
            "JOIN movie_actor ma2 ON m.movie_id = ma2.movie_id " +
            "JOIN actor a2 ON ma2.actor_id = a2.actor_id " +
            "WHERE t.type_name = #{type} " +
            "AND a1.actor_id < a2.actor_id " +
            "GROUP BY a1.actor_id, a2.actor_id " +
            "ORDER BY TotalComments DESC " +
            "LIMIT #{limit}")
    List<ActorPairWithCommentsDTO> findMostPopularActorPairs(@Param("type") String type, @Param("limit") int limit);



    // 查询某类别的电影中，与最多演员组合合作的导演及其合作的电影总数
    @Select("SELECT " +
            "d.director_name AS DirectorName, " +
            "CONCAT(a1.actor_name, ' & ', a2.actor_name) AS actor_name, " +
            "COUNT(m.movie_id) AS movie_count " +
            "FROM movie m " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "JOIN movie_director md ON m.movie_id = md.movie_id " +
            "JOIN director d ON md.director_id = d.director_id " +
            "JOIN movie_actor ma1 ON m.movie_id = ma1.movie_id " +
            "JOIN actor a1 ON ma1.actor_id = a1.actor_id " +
            "JOIN movie_actor ma2 ON m.movie_id = ma2.movie_id " +
            "JOIN actor a2 ON ma2.actor_id = a2.actor_id " +
            "WHERE t.type_name = #{type} " +
            "AND a1.actor_id < a2.actor_id " +
            "GROUP BY d.director_id, actor_name " +
            "ORDER BY movie_count DESC " +
            "LIMIT #{limit}")
    List<DirectorActorCooperationDTO> findTopDirectorsWithActorCollaboration(@Param("type") String type,@Param("limit") int limit);


    //查询某演员组合中，参与正面评价最多的某类别电影
    @Select("SELECT " +
            "CONCAT(a1.actor_name, ' & ', a2.actor_name) AS actor, " +
            "SUM(m.good_review_count) AS movie_count " + // 使用 good_review_count 字段
            "FROM movie m " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "JOIN movie_actor ma1 ON m.movie_id = ma1.movie_id " +
            "JOIN actor a1 ON ma1.actor_id = a1.actor_id " +
            "JOIN movie_actor ma2 ON m.movie_id = ma2.movie_id " +
            "JOIN actor a2 ON ma2.actor_id = a2.actor_id " +
            "WHERE t.type_name = #{type} " +
            "AND a1.actor_id < a2.actor_id " + // 避免重复的演员组合
            "GROUP BY actor " +
            "ORDER BY movie_count DESC "+
            "LIMIT #{limit}")
    List<ActorMovieCountDTO> findTopActorPairsWithPositiveReviews(@Param("type") String type, @Param("limit") int limit);


    // 查询某类型电影中评分最高的演员组合及其参与的电影数量
    @Select("SELECT " +
            "CONCAT(a1.actor_name, ' & ', a2.actor_name) AS ActorPair, " +
            "AVG(m.grade) AS AverageScore, " + // 使用 movie 表中的 grade 字段
            "COUNT(DISTINCT m.movie_id) AS MovieCount " +
            "FROM movie m " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "JOIN movie_actor ma1 ON m.movie_id = ma1.movie_id " +
            "JOIN actor a1 ON ma1.actor_id = a1.actor_id " +
            "JOIN movie_actor ma2 ON m.movie_id = ma2.movie_id " +
            "JOIN actor a2 ON ma2.actor_id = a2.actor_id " +
            "WHERE t.type_name = #{type} " +
            "AND a1.actor_id < a2.actor_id " + // 避免重复的演员组合
            "GROUP BY ActorPair " +
            "ORDER BY AverageScore DESC, MovieCount DESC " +
            "LIMIT #{limit}")
    List<ActorGradeCountDTO> findTopActorPairsWithHighestScore(@Param("type") String type, @Param("limit") int limit);






}
