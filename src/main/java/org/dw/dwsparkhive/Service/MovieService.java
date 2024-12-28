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

    // 2. 获取指定电影的版本数量
    public QueryResultDTO<List<String>> getMovieVersionCount(String movieName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT m.name AS MovieName, COUNT(mv.versionId) AS VersionCount, COLLECT_LIST(mv.versionId) AS VersionIds " +
                        "FROM Movie m " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "WHERE m.name LIKE '%%%s%%' " +
                        "GROUP BY m.name", escapeSQL(movieName));

        Dataset<Row> result = sparkSession.sql(query);

        List<String> data = result.collectAsList().stream()
                .map(row -> {
                    String movieNameResult = row.getAs("MovieName");
                    Long versionCount = row.getAs("VersionCount");
                    List<String> versionIds = row.getList(row.fieldIndex("VersionIds"));
                    return "Movie: " + movieNameResult + ", Version Count: " + versionCount + ", Versions: " + String.join(", ", versionIds);
                })
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    // 6. 获取指定类型的电影总数
    public QueryResultDTO<List<String>> getMovieCountByType(String typeName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT COUNT(m.movieId) AS MovieCount, COLLECT_LIST(m.name) AS MovieNames " +
                        "FROM Movie m " +
                        "JOIN Movie_Type mt ON m.movieId = mt.movieId " +
                        "JOIN Type t ON mt.typeId = t.typeId " +
                        "WHERE t.typeName = '%s' " +
                        "GROUP BY t.typeName", escapeSQL(typeName));

        Dataset<Row> result = sparkSession.sql(query);

        List<String> data = result.collectAsList().stream()
                .map(row -> {
                    String count = row.getAs("MovieCount").toString();
                    List<String> movieNames = row.getList(row.fieldIndex("MovieNames"));
                    return "Count: " + count + ", Movies: " + String.join(", ", movieNames);
                })
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    // 10. 查找哈利波特系列的电影相关统计
    public QueryResultDTO<List<String>> getHarryPotterMovies() {
        long startTime = System.currentTimeMillis();

        String query =
                "SELECT COUNT(m.name) AS movieCount, " +
                        "       COUNT(DISTINCT m.name) AS movieDistinctCount, " +
                        "       COUNT(mv.versionId) AS VersionCount, " +
                        "       COUNT(DISTINCT mv.versionId) AS VersionDistinctCount " +
                        "FROM Movie m " +
                        "JOIN Movie_Version mv ON m.movieId = mv.movieId " +
                        "WHERE m.name LIKE '%Harry Potter%'";

        Dataset<Row> result = sparkSession.sql(query);
        Row row = result.first();

        List<String> counts = Arrays.asList(
                "Movie Count: " + row.getAs("movieCount").toString(),
                "Distinct Movie Count: " + row.getAs("movieDistinctCount").toString(),
                "Version Count: " + row.getAs("VersionCount").toString(),
                "Distinct Version Count: " + row.getAs("VersionDistinctCount").toString()
        );

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(counts, duration);
    }

    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
}
