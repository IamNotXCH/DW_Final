package org.dw.dwsparkhive.Service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    @Autowired
    private SparkSession sparkSession;

    // 3. 获取指定导演的电影数量
    public QueryResultDTO<List<String>> getDirectorMovieCount(String directorName) {
        long startTime = System.currentTimeMillis();

        String query = String.format(
                "SELECT COUNT(md.movieId) AS MovieCount, COLLECT_LIST(m.name) AS MovieNames " +
                        "FROM Director d " +
                        "JOIN Movie_Director md ON d.directorId = md.directorId " +
                        "JOIN Movie m ON md.movieId = m.movieId " +
                        "WHERE d.directorName = '%s' " +
                        "GROUP BY d.directorName", escapeSQL(directorName));

        Dataset<Row> result = sparkSession.sql(query);

        List<String> data = result.collectAsList().stream()
                .map(row -> {
                    Long count = row.getAs("MovieCount");
                    List<String> movieNames = row.getList(row.fieldIndex("MovieNames"));
                    return "Count: " + count + ", Movies: " + String.join(", ", movieNames);
                })
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return new QueryResultDTO<>(data, duration);
    }

    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }
}
