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
public class CollaborationService {

    @Autowired
    private SparkSession sparkSession;

    //5 经常一起合作的导演和演员有哪些
    public QueryResultDTO<List<List<String>>> getFrequentDirectorActorPairs() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT d.directorName AS DirectorName, " +
                        "       a.actorName AS ActorName, " +
                        "       COUNT(m.movieId) AS MovieCount " +
                        "FROM Director d " +
                        "JOIN Movie_Director md ON d.directorId = md.directorId " +
                        "JOIN Movie m ON md.movieId = m.movieId " +
                        "JOIN Movie_Actor ma ON m.movieId = ma.movieId " +
                        "JOIN Actor a ON ma.actorId = a.actorId " +
                        "GROUP BY d.directorName, a.actorName " +
                        "HAVING COUNT(m.movieId) > 1 " +
                        "ORDER BY MovieCount DESC" +
                        "LIMIT 100";

        Dataset<Row> result = sparkSession.sql(query);

        List<List<String>> directorActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("DirectorName"),
                        row.getAs("ActorName"),
                        row.getAs("MovieCount").toString()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(directorActorPairs, duration);
    }

    //9 查询某类别的电影中，与最多演员组合合作的导演及其合作的电影总数 
    public QueryResultDTO<List<List<Object>>> getTopDirectorByMovieType(String typeName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT d.directorName AS DirectorName, COUNT(m.movieId) AS MovieCount " +
                        "FROM Type t " +
                        "JOIN Movie_Type mt ON t.typeId = mt.typeId " +
                        "JOIN Movie m ON mt.movieId = m.movieId " +
                        "JOIN Movie_Director md ON m.movieId = md.movieId " +
                        "JOIN Director d ON md.directorId = d.directorId " +
                        "WHERE t.typeName = '%s' " +
                        "GROUP BY d.directorName " +
                        "ORDER BY MovieCount DESC " +
                        "LIMIT 1", escapeSQL(typeName));

        Dataset<Row> result = sparkSession.sql(query);

        List<List<Object>> topDirector = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("DirectorName"),
                        row.getAs("MovieCount")
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(topDirector, duration);
    }

    //9 查询某类型电影中评分最高的演员组合及其参与的电影数量
    public QueryResultDTO<List<List<Object>>> getTopActorPairsByType(String typeName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "WITH ActorPairCounts AS ( " +
                        "    SELECT a1.actorName AS Actor1, a2.actorName AS Actor2, COUNT(m.movieId) AS MovieCount " +
                        "    FROM Type t " +
                        "    JOIN Movie_Type mt ON t.typeId = mt.typeId " +
                        "    JOIN Movie m ON mt.movieId = m.movieId " +
                        "    JOIN Movie_Actor ma1 ON m.movieId = ma1.movieId " +
                        "    JOIN Actor a1 ON ma1.actorId = a1.actorId " +
                        "    JOIN Movie_Actor ma2 ON m.movieId = ma2.movieId " +
                        "    JOIN Actor a2 ON ma2.actorId = a2.actorId " +
                        "    WHERE t.typeName = '%s' AND a1.actorId < a2.actorId " +
                        "    GROUP BY a1.actorName, a2.actorName " +
                        "), MaxCount AS ( " +
                        "    SELECT MAX(MovieCount) AS MaxMovieCount FROM ActorPairCounts " +
                        ") " +
                        "SELECT ap.Actor1, ap.Actor2, ap.MovieCount " +
                        "FROM ActorPairCounts ap " +
                        "JOIN MaxCount mc ON ap.MovieCount = mc.MaxMovieCount " +
                        "ORDER BY ap.MovieCount DESC", escapeSQL(typeName));

        Dataset<Row> result = sparkSession.sql(query);

        List<List<Object>> topActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(
                        row.getAs("Actor1"),
                        row.getAs("Actor2"),
                        row.getAs("MovieCount")
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(topActorPairs, duration);
    }


    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
}
