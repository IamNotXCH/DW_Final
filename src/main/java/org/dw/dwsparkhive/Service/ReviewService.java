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
public class ReviewService {

    @Autowired
    private SparkSession sparkSession;

    // 7 用户评分高于4.5的电影
    public QueryResultDTO<List<String>> getHighScoreMovies() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT m.name AS MovieName, COUNT(r.comment_id) AS HighScoreCount " +
                        "FROM Movie m " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "JOIN Movie_Version_Web mvw ON mv.versionId = mvw.versionId " +
                        "JOIN Review r ON mvw.ASIN = r.product_id " +
                        "WHERE CAST(r.score AS DOUBLE) > 4.5 " +
                        "GROUP BY m.name " +
                        "ORDER BY HighScoreCount DESC";

        Dataset<Row> result = sparkSession.sql(query);

        List<Row> rows = result.collectAsList();
        List<String> data = rows.stream()
                .map(row -> {
                    String movieName = row.getAs("MovieName");
                    Long highScoreCount = row.getAs("HighScoreCount");
                    return movieName + ": " + highScoreCount;
                })
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    // 7. 用户评价中有正面评价的电影
    public QueryResultDTO<List<Object>> getPositiveReviewedMovies() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT DISTINCT m.name AS MovieName " +
                        "FROM Movie m " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "JOIN Movie_Version_Web mvw ON mv.versionId = mvw.versionId " +
                        "JOIN Review r ON mvw.ASIN = r.product_id " +
                        "WHERE CAST(r.score AS DOUBLE) > 4 AND r.is_positive = 'True'";

        Dataset<Row> result = sparkSession.sql(query);

        List<Object> data = result.collectAsList().stream()
                .map(row -> row.getAs("MovieName"))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    // 9. 查询某演员组合中，参与正面评价最多的某类别电影
    public QueryResultDTO<List<List<String>>> getTopActorPairsByTypeWithMostPositiveReviews(String typeName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT a1.actorName AS Actor1, a2.actorName AS Actor2, COUNT(r.comment_id) AS PositiveReviewCount " +
                        "FROM Type t " +
                        "JOIN Movie_Type mt ON t.typeId = mt.typeId " +
                        "JOIN Movie m ON mt.movieId = m.movieId " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "JOIN Movie_Version_Web mvw ON mv.versionId = mvw.versionId " +
                        "JOIN Review r ON mvw.ASIN = r.product_id " +
                        "JOIN Movie_Actor ma1 ON m.movieId = ma1.movieId " +
                        "JOIN Actor a1 ON ma1.actorId = a1.actorId " +
                        "JOIN Movie_Actor ma2 ON m.movieId = ma2.movieId " +
                        "JOIN Actor a2 ON ma2.actorId = a2.actorId " +
                        "WHERE t.typeName = '%s' " +
                        "AND a1.actorId < a2.actorId " + // 确保每对组合唯一
                        "AND r.score > 4 " +
                        "AND r.is_positive = 'True' " +
                        "AND r.comment_id <> 'Unknown' " + // 忽略评论数为 "Unknown" 的情况
                        "GROUP BY a1.actorName, a2.actorName " +
                        "ORDER BY PositiveReviewCount DESC " +
                        "LIMIT 100", escapeSQL(typeName));

        Dataset<Row> result = sparkSession.sql(query);

        List<List<String>> popularActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Actor1"),
                        row.getAs("Actor2"),
                        row.getAs("PositiveReviewCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(popularActorPairs, duration);
    }

    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
}
