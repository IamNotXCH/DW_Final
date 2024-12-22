package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCommentService {

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

    // 查询用户评分3分以上的电影列表
    public QueryResultDTO<List<String>> getMoviesWithAverageScoreAbove(int score) {
        String sql = "SELECT m.name " +
                "FROM Movie m " +
                "JOIN Review r ON CAST(r.product_id AS INT) = m.movieId " +
                "GROUP BY m.name " +
                "HAVING AVG(r.score) > ?";
        List<String> movies = jdbcTemplate.query(sql, new Object[]{score}, (rs, rowNum) -> rs.getString("name"));
        long startTime = System.currentTimeMillis();
        // 查询已经在 above 进行
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(movies, queryTime);
    }

    // 查询用户评价中有正面评价的电影列表
    public QueryResultDTO<List<String>> getMoviesWithPositiveComments() {
        String sql = "SELECT DISTINCT m.name " +
                "FROM Movie m " +
                "JOIN Review r ON CAST(r.product_id AS INT) = m.movieId " +
                "WHERE r.is_positive = TRUE";
        List<String> movies = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
        long startTime = System.currentTimeMillis();
        // 查询已经在 above 进行
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(movies, queryTime);
    }
}
