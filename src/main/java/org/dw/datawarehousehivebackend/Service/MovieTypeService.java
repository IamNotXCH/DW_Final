package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieTypeService {

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

    // 按电影名称查询版本数量
    public QueryResultDTO<Integer> getVersionCountByMovieName(String movieName) {
        String sql = "SELECT COUNT(DISTINCT mv.versionId) AS count FROM Movie m " +
                "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                "WHERE m.name = ?";
        return executeQuery(sql, new Object[]{movieName}, Integer.class);
    }

    // 按电影类别查询电影数量
    public QueryResultDTO<Integer> getMovieCountByType(String typeName) {
        String sql = "SELECT COUNT(DISTINCT m.movieId) AS count FROM Movie m " +
                "JOIN Movie_Type mt ON m.movieId = mt.movieId " +
                "JOIN Type t ON mt.typeId = t.typeId " +
                "WHERE t.typeName = ?";
        return executeQuery(sql, new Object[]{typeName}, Integer.class);
    }

    // 获取所有电影类别及其对应的电影数量
    public QueryResultDTO<List<String>> getMovieCountForAllTypes() {
        String sql = "SELECT t.typeName, COUNT(DISTINCT m.movieId) AS count FROM Movie m " +
                "JOIN Movie_Type mt ON m.movieId = mt.movieId " +
                "JOIN Type t ON mt.typeId = t.typeId " +
                "GROUP BY t.typeName " +
                "ORDER BY count DESC";
        List<String> typeCounts = jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getString("typeName") + ": " + rs.getInt("count")
        );
        long startTime = System.currentTimeMillis();
        // 查询已经在 above 进行
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        return new QueryResultDTO<>(typeCounts, queryTime);
    }
}
