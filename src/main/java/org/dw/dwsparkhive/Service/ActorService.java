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
public class ActorService {

    @Autowired
    private SparkSession sparkSession;

    // 4. XX演员参演了多少电影，并列出前五部
    public QueryResultDTO<List<Object>> getActorMovieCount(String actorName) {
        String query = String.format(
                "SELECT a.Name AS ActorName, COUNT(m) AS MovieCount, ARRAY_AGG(m.Name) AS TopMovies " +
                        "FROM Actor a JOIN Movie m ON a.Id = m.ActorId " +
                        "WHERE a.Name = '%s' " +
                        "GROUP BY a.Name " +
                        "ORDER BY MovieCount DESC " +
                        "LIMIT 5", actorName);

        Dataset<Row> result = sparkSession.sql(query);
        List<Object> actorMovies = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), row.getLong(1), row.getList(2)))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(actorMovies, System.currentTimeMillis());
    }

    // 7. 用户评价中有正面评价的电影
    public QueryResultDTO<List<String>> getPositiveReviewedMovies() {
        String query =
                "SELECT DISTINCT m.Name AS MovieName " +
                        "FROM Movie m JOIN Version v ON m.Id = v.MovieId " +
                        "JOIN Review r ON v.Id = r.VersionId " +
                        "WHERE r.score > 4 AND r.is_positive = true";

        Dataset<Row> result = sparkSession.sql(query);
        List<String> movieNames = result.collectAsList().stream()
                .map(row -> row.getString(0))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(movieNames, System.currentTimeMillis());
    }
}