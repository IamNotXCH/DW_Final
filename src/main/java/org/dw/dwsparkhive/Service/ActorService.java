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

    // 4. 获取指定演员参演的电影数量，并列出前五部电影
    public QueryResultDTO<List<Object>> getActorMovieCount(String actorName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT m.name AS MovieName " +
                        "FROM Actor a " +
                        "JOIN Movie_Actor ma ON a.actorId = ma.actorId " +
                        "JOIN Movie m ON ma.movieId = m.movieId " +
                        "WHERE a.actorName = '%s' " +
                        "LIMIT 5", escapeSQL(actorName));

        Dataset<Row> result = sparkSession.sql(query);

        List<Object> data = result.collectAsList().stream()
                .map(row -> row.getAs("MovieName"))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    // 5. 常合作的演员组合
    public QueryResultDTO<List<List<String>>> getFrequentActorPairs() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT " +
                        "    a1.actorName AS Actor1, " +
                        "    a2.actorName AS Actor2, " +
                        "    COUNT(*) AS MovieCount " +
                        "FROM Actor a1 " +
                        "JOIN Movie_Actor ma1 ON a1.actorId = ma1.actorId " +
                        "JOIN Movie_Actor ma2 ON ma1.movieId = ma2.movieId " +
                        "JOIN Actor a2 ON ma2.actorId = a2.actorId " +
                        "WHERE a1.actorId < a2.actorId " +
                        "GROUP BY a1.actorName, a2.actorName " +
                        "HAVING COUNT(*) > 1 " +
                        "ORDER BY MovieCount DESC " +
                        "LIMIT 100";

        Dataset<Row> result = sparkSession.sql(query);

        List<List<String>> actorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Actor1"),
                        row.getAs("Actor2"),
                        String.valueOf(row.getLong(row.fieldIndex("MovieCount")))
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(actorPairs, duration);
    }


    // 8. 如果要拍一部XXX类型的电影，最受关注的演员组合
    public QueryResultDTO<List<List<String>>> getPopularActorPairsByType(String typeName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT a1.actorName AS Actor1, a2.actorName AS Actor2, COUNT(r.comment_id) AS CommentCount " +
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
                        "WHERE t.typeName = '%s' AND a1.actorId < a2.actorId " +
                        "GROUP BY a1.actorName, a2.actorName " +
                        "ORDER BY CommentCount DESC " +
                        "LIMIT 100", escapeSQL(typeName));

        Dataset<Row> result = sparkSession.sql(query);

        List<List<String>> popularActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Actor1"),
                        row.getAs("Actor2"),
                        row.getAs("CommentCount").toString()
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
