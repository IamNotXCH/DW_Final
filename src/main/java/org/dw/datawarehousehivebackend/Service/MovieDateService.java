package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MovieDateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 通用方法来执行查询并记录时间
    private <T> QueryResultDTO<T> executeQuery(String sql, Object[] params, Class<T> requiredType) {
        long startTime = System.currentTimeMillis();
        T result = jdbcTemplate.queryForObject(sql, params, requiredType);
        long endTime = System.currentTimeMillis();
        return new QueryResultDTO<>(result, endTime - startTime);
    }

    // 按年份查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYear(int year) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Dates d ON m.dateId = d.dateId " +
                "WHERE d.year = ?";
        return executeQuery(sql, new Object[]{year}, Integer.class);
    }

    // 按年份和月份查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearAndMonth(int year, int month) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Dates d ON m.dateId = d.dateId " +
                "WHERE d.year = ? AND d.month = ?";
        return executeQuery(sql, new Object[]{year, month}, Integer.class);
    }

    // 按年份和季度查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearAndSeason(int year, int season) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Dates d ON m.dateId = d.dateId " +
                "WHERE d.year = ? AND d.season = ?";
        return executeQuery(sql, new Object[]{year, season}, Integer.class);
    }

    // 按年份和周数查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearAndWeek(int year, int week) {
        String sql = "SELECT COUNT(*) AS count FROM Movie m " +
                "JOIN Dates d ON m.dateId = d.dateId " +
                "WHERE d.year = ? AND d.week = ?";
        return executeQuery(sql, new Object[]{year, week}, Integer.class);
    }
}
