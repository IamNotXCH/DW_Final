package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieActDirService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 通用方法来执行查询并记录时间
    private <T> QueryResultDTO<T> executeQuery(String sql, Object[] params, Class<T> requiredType) {
        long startTime = System.currentTimeMillis();
        T result = jdbcTemplate.queryForObject(sql, params, requiredType);
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(result, queryTime);
    }

    // 通用方法来执行查询并记录时间，返回列表
    private <T> QueryResultDTO<List<T>> executeQueryForList(String sql, Object[] params, Class<T> elementType) {
        long startTime = System.currentTimeMillis();
        List<T> result = jdbcTemplate.queryForList(sql, params, elementType);
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(result, queryTime);
    }

    // 按导演名称查询电影数量
    public QueryResultDTO<Integer> getMovieCountByDirector(String directorName) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Movie_Director md ON m.movieId = md.movieId " +
                "JOIN Director d ON md.directorId = d.directorId " +
                "WHERE d.directorName = ?";
        return executeQuery(sql, new Object[]{directorName}, Integer.class);
    }

    // 按演员名称查询主演电影数量
    // 假设所有 Movie_Actor 记录都是主演
    public QueryResultDTO<Integer> getStarredMovieCountByActor(String actorName) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Movie_Actor ma ON m.movieId = ma.movieId " +
                "JOIN Actor a ON ma.actorId = a.actorId " +
                "WHERE a.actorName = ?";
        return executeQuery(sql, new Object[]{actorName}, Integer.class);
    }

    // 按演员名称查询参演电影数量
    // 由于表结构中没有区分主演和参演，这里假设所有 Movie_Actor 记录都是参演
    public QueryResultDTO<Integer> getParticipatedMovieCountByActor(String actorName) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Movie_Actor ma ON m.movieId = ma.movieId " +
                "JOIN Actor a ON ma.actorId = a.actorId " +
                "WHERE a.actorName = ?";
        return executeQuery(sql, new Object[]{actorName}, Integer.class);
    }

    // 获取经常一起合作的演员列表
    // 定义经常合作为合作次数超过一定阈值，比如5次
    public QueryResultDTO<List<String>> getFrequentCollaboratingActors(int minCollaborationCount) {
        String sql = "SELECT a2.actorName, COUNT(*) AS collaborationCount " +
                "FROM Actor_Actor_Cooperation aac " +
                "JOIN Actor a1 ON aac.actorId1 = a1.actorId " +
                "JOIN Actor a2 ON aac.actorId2 = a2.actorId " +
                "WHERE aac.count >= ? " +
                "GROUP BY a2.actorName " +
                "ORDER BY collaborationCount DESC";
        return executeQueryForList(sql, new Object[]{minCollaborationCount}, String.class);
    }

    // 获取经常一起合作的导演和演员列表
    // 定义经常合作为合作次数超过一定阈值，比如3次
    public QueryResultDTO<List<String>> getFrequentDirectorActorCollaborations(int minCollaborationCount) {
        String sql = "SELECT d.directorName, a.actorName, dac.count " +
                "FROM Director_Actor_Cooperation dac " +
                "JOIN Director d ON dac.directorId = d.directorId " +
                "JOIN Actor a ON dac.actorId = a.actorId " +
                "WHERE dac.count >= ? " +
                "ORDER BY dac.count DESC";
        // 这里返回格式为 "DirectorName - ActorName (Count)"
        List<String> collaborations = jdbcTemplate.query(sql, new Object[]{minCollaborationCount},
                (rs, rowNum) -> rs.getString("directorName") + " - " + rs.getString("actorName") + " (" + rs.getInt("count") + ")");
        long startTime = System.currentTimeMillis();
        // Already executed query above
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(collaborations, queryTime);
    }
}
