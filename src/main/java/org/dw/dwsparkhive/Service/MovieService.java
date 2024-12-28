package org.dw.dwsparkhive.Service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private SparkSession sparkSession;

    // 2. XX电影共有多少版本
    public QueryResultDTO<Long> getMovieVersionCount(String movieName) {
        String query = String.format(
                "SELECT COUNT(DISTINCT v.Version) AS VersionCount " +
                        "FROM Movie m JOIN Version v ON m.Id = v.MovieId " +
                        "WHERE m.Name LIKE '%%%s%%'", movieName);

        Dataset<Row> result = sparkSession.sql(query);
        return new QueryResultDTO<>(result.first().getLong(0), System.currentTimeMillis());
    }

    // 6. 某类别的电影总数
    public QueryResultDTO<Long> getMovieCountByType(String typeName) {
        String query = String.format(
                "SELECT COUNT(m) AS MovieCount " +
                        "FROM Movie m JOIN Type t ON m.TypeId = t.Id " +
                        "WHERE t.Name = '%s'", typeName);

        Dataset<Row> result = sparkSession.sql(query);
        return new QueryResultDTO<>(result.first().getLong(0), System.currentTimeMillis());
    }

    // 10. 查找哈利波特系列的电影
    public QueryResultDTO<List<Object>> getHarryPotterMovies() {
        String query =
                "SELECT COUNT(m.Name) AS movieCount, COUNT(DISTINCT m.Name) AS movieDistinctCount, " +
                        "COUNT(v.Version) AS VersionCount, COUNT(DISTINCT v.Version) AS VersionDistinctCount " +
                        "FROM Movie m JOIN Version v ON m.Id = v.MovieId " +
                        "WHERE m.Name LIKE '%Harry Potter%'";

        Dataset<Row> result = sparkSession.sql(query);
        List<Object> counts = Arrays.asList(
                result.first().getLong(0),
                result.first().getLong(1),
                result.first().getLong(2),
                result.first().getLong(3)
        );

        return new QueryResultDTO<>(counts, System.currentTimeMillis());
    }
}