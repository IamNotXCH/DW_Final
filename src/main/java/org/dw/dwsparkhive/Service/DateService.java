package org.dw.dwsparkhive.Service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateService {

    private static final Logger logger = LoggerFactory.getLogger(DateService.class);

    @Autowired
    private SparkSession sparkSession;

    // 通用方法来执行查询并记录时间
    private QueryResultDTO<Integer> executeCountQuery(String sql) {
        long startTime = System.currentTimeMillis();
        Dataset<Row> df = sparkSession.sql(sql);
        Long count = df.collectAsList().get(0).getLong(0);
        long endTime = System.currentTimeMillis();
        return new QueryResultDTO<>(count.intValue(), endTime - startTime);
    }

    // 按年份查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYear(int year) {
        String sql = String.format(
                "SELECT COUNT(*) AS count FROM Movie m JOIN Dates d ON m.dateId = d.dateId WHERE d.year = %d",
                year
        );
        return executeCountQuery(sql);
    }

    // 按年份和月份查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearAndMonth(int year, int month) {
        String sql = String.format(
                "SELECT COUNT(*) AS count FROM Movie m JOIN Dates d ON m.dateId = d.dateId WHERE d.year = %d AND d.month = %d",
                year, month
        );
        return executeCountQuery(sql);
    }

    // 按年份和季度查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearAndSeason(int year, int season) {
        String sql = String.format(
                "SELECT COUNT(*) AS count FROM Movie m JOIN Dates d ON m.dateId = d.dateId WHERE d.year = %d AND d.season = %d",
                year, season
        );
        return executeCountQuery(sql);
    }

    // 按年份、月份和日期查询电影数量
    public QueryResultDTO<Integer> getMovieCountByYearMonthDay(int year, int month, int day) {
        String sql = String.format(
                "SELECT COUNT(*) AS count FROM Movie m JOIN Dates d ON m.dateId = d.dateId WHERE d.year = %d AND d.month = %d AND d.day = %d",
                year, month, day
        );
        return executeCountQuery(sql);
    }

}
