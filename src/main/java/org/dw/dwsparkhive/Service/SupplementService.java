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

    // 0 按演员统计其参演的电影
    public QueryResultDTO<List<List<String>>> getActorMovieCounts() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT a.actorName AS Actor, COUNT(ma.movieId) AS MovieCount " +
                        "FROM Actor a " +
                        "JOIN Movie_Actor ma ON a.actorId = ma.actorId " +
                        "JOIN Movie m ON ma.movieId = m.movieId " +
                        "GROUP BY a.actorName " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> actorCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Actor"),
                        row.getAs("MovieCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(actorCounts, duration);
    }

    // 0 按导演统计其导演的电影
    public QueryResultDTO<List<List<String>>> getDirectorMovieCounts() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT d.directorName AS Director, COUNT(md.movieId) AS MovieCount " +
                        "FROM Director d " +
                        "JOIN Movie_Director md ON d.directorId = md.directorId " +
                        "JOIN Movie m ON md.movieId = m.movieId " +
                        "GROUP BY d.directorName " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> directorCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Director"),
                        row.getAs("MovieCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(directorCounts, duration);
    }

    // 0 按类型统计电影数量
    public QueryResultDTO<List<List<String>>> getMovieCountsByType() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT t.typeName AS Genre, COUNT(m.movieId) AS MovieCount " +
                        "FROM Type t " +
                        "JOIN Movie_Type mt ON t.typeId = mt.typeId " +
                        "JOIN Movie m ON mt.movieId = m.movieId " +
                        "GROUP BY t.typeName " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> typeCounts = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Genre"),
                        row.getAs("MovieCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(typeCounts, duration);
    }

    //0 查询所有有多个版本的电影
    public QueryResultDTO<List<List<String>>> getMoviesWithMultipleVersions() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT m.name AS MovieName, COUNT(mv.versionId) AS VersionCount " +
                        "FROM Movie m " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "GROUP BY m.name " +
                        "HAVING COUNT(mv.versionId) > 1 " +
                        "ORDER BY VersionCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> multipleVersionsMovies = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("MovieName"),
                        row.getAs("VersionCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(multipleVersionsMovies, duration);
    }

    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
}
