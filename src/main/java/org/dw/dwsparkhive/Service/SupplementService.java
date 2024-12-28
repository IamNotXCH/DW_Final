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
public class SupplementService {

    @Autowired
    private SparkSession sparkSession;

    // 0. 按演员统计其参演的电影
    public QueryResultDTO<List<List<String>>> getActorMovieCounts() {
        String query =
                "SELECT a.Name AS Actor, COUNT(m) AS MovieCount " +
                        "FROM Actor a JOIN Movie m ON a.Id = m.ActorId " +
                        "GROUP BY a.Name " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> actorCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), String.valueOf(row.getLong(1))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(actorCounts, System.currentTimeMillis());
    }

    // 0. 按导演统计其导演的电影
    public QueryResultDTO<List<List<String>>> getDirectorMovieCounts() {
        String query =
                "SELECT d.Name AS Director, COUNT(m) AS MovieCount " +
                        "FROM Director d JOIN Movie m ON d.Id = m.DirectorId " +
                        "GROUP BY d.Name " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> directorCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), String.valueOf(row.getLong(1))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(directorCounts, System.currentTimeMillis());
    }

    // 0. 按类型统计电影数量
    public QueryResultDTO<List<List<String>>> getMovieCountsByType() {
        String query =
                "SELECT t.Name AS Genre, COUNT(m) AS MovieCount " +
                        "FROM Type t JOIN Movie m ON t.Id = m.TypeId " +
                        "GROUP BY t.Name " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> typeCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), String.valueOf(row.getLong(1))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(typeCounts, System.currentTimeMillis());
    }

    // 0. 查询所有有多个版本的电影
    public QueryResultDTO<List<List<Object>>> getMoviesWithMultipleVersions() {
        String query =
                "SELECT m, COLLECT(v) AS versions, COUNT(v) AS versionCount " +
                        "FROM Movie m JOIN Version v ON m.Id = v.MovieId " +
                        "GROUP BY m " +
                        "HAVING versionCount > 1";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<Object>> multipleVersionsMovies = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.get(0), row.getList(1), row.getLong(2)))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(multipleVersionsMovies, System.currentTimeMillis());
    }
}