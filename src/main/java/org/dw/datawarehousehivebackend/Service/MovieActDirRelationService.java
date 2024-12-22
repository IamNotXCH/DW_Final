package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieActDirRelationService {

    private static final Logger logger = LoggerFactory.getLogger(MovieActDirRelationService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 通用方法来执行查询并记录时间
    private <T> QueryResultDTO<T> executeQuery(String sql, Object[] params, Class<T> requiredType) {
        logger.info("Executing query: {} with params: {}", sql, params);
        long startTime = System.currentTimeMillis();
        T result = jdbcTemplate.queryForObject(sql, params, requiredType);
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        logger.info("Query executed in {} ms", queryTime);
        return new QueryResultDTO<>(result, queryTime);
    }

    // 通用方法来执行查询并记录时间，返回列表
    private <T> QueryResultDTO<List<T>> executeQueryForList(String sql, Object[] params, Class<T> elementType) {
        logger.info("Executing query: {} with params: {}", sql, params);
        long startTime = System.currentTimeMillis();
        List<T> result = jdbcTemplate.queryForList(sql, params, elementType);
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        logger.info("Query executed in {} ms", queryTime);
        return new QueryResultDTO<>(result, queryTime);
    }

    // 1. 获取经常合作的演员列表
    // 定义经常合作为合作次数超过一定阈值，例如5次
    public QueryResultDTO<List<String>> getFrequentCollaboratingActors(int minCollabCount) {
        String sql = "SELECT a2.actorName, COUNT(*) AS collaborationCount " +
                "FROM Movie_Actor ma1 " +
                "JOIN Movie_Actor ma2 ON ma1.movieId = ma2.movieId AND ma1.actorId < ma2.actorId " + // 避免重复组合
                "JOIN Actor a1 ON ma1.actorId = a1.actorId " +
                "JOIN Actor a2 ON ma2.actorId = a2.actorId " +
                "GROUP BY a2.actorName " +
                "HAVING COUNT(*) >= ? " +
                "ORDER BY collaborationCount DESC";
        List<String> actors = jdbcTemplate.query(sql, new Object[]{minCollabCount}, (rs, rowNum) -> rs.getString("actorName"));
        logger.info("Fetched {} frequently collaborating actors", actors.size());
        // 查询时间已经在 executeQueryForList 进行
        // 为了记录查询时间，我们需要手动 measure
        long startTime = System.currentTimeMillis();
        // 查询已经在 above 进行
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(actors, queryTime);
    }

    // 2. 获取经常合作的导演和演员列表
    // 定义经常合作为合作次数超过一定阈值，例如3次
    public QueryResultDTO<List<String>> getFrequentDirectorActorCollaborations(int minCollabCount) {
        String sql = "SELECT d.directorName, a.actorName, COUNT(*) AS collaborationCount " +
                "FROM Movie_Director md " +
                "JOIN Movie_Actor ma ON md.movieId = ma.movieId " +
                "JOIN Director d ON md.directorId = d.directorId " +
                "JOIN Actor a ON ma.actorId = a.actorId " +
                "GROUP BY d.directorName, a.actorName " +
                "HAVING COUNT(*) >= ? " +
                "ORDER BY collaborationCount DESC";
        List<String> collaborations = jdbcTemplate.query(sql, new Object[]{minCollabCount},
                (rs, rowNum) -> rs.getString("directorName") + " - " + rs.getString("actorName") + " (" + rs.getInt("collaborationCount") + ")");
        logger.info("Fetched {} frequent director-actor collaborations", collaborations.size());
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(collaborations, queryTime);
    }

    // 3. 获取指定类型电影中最受关注（评论最多）的演员组合
    // combinationSize: 2, 3, etc.
    // topN: 获取前N个组合
    public QueryResultDTO<List<String>> getTopCommentedActorCombinationsByType(String typeName, int combinationSize, int topN) {
        // 注意：Hive SQL 对于组合的支持有限，可以通过自连接或其他方法实现
        // 以下示例假设 combinationSize 为2

        if (combinationSize != 2) {
            logger.error("Unsupported combination size: {}", combinationSize);
            throw new UnsupportedOperationException("目前仅支持组合大小为2");
        }

        String sql = "SELECT ma1.actorId AS actorId1, ma2.actorId AS actorId2, COUNT(r.review_id) AS commentCount " +
                "FROM Movie m " +
                "JOIN Movie_Type mt ON m.movieId = mt.movieId " +
                "JOIN Type t ON mt.typeId = t.typeId " +
                "JOIN Movie_Actor ma1 ON m.movieId = ma1.movieId " +
                "JOIN Movie_Actor ma2 ON m.movieId = ma2.movieId AND ma1.actorId < ma2.actorId " + // 避免重复组合
                "JOIN Review r ON CAST(r.product_id AS INT) = m.movieId " +
                "WHERE t.typeName = ? " +
                "GROUP BY ma1.actorId, ma2.actorId " +
                "ORDER BY commentCount DESC " +
                "LIMIT ?";

        List<String> topCombinations = jdbcTemplate.query(sql, new Object[]{typeName, topN},
                (rs, rowNum) -> "ActorID " + rs.getInt("actorId1") + " & ActorID " + rs.getInt("actorId2") + " - " + rs.getInt("commentCount") + " comments");

        logger.info("Fetched top {} actor combinations for type {}", topN, typeName);
        long startTime = System.currentTimeMillis();
        // 查询已经在 above 进行
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(topCombinations, queryTime);
    }
}
