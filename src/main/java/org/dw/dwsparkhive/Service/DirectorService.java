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
public class DirectorService {

    @Autowired
    private SparkSession sparkSession;

    // 3. XX导演共有多少电影
    public QueryResultDTO<Long> getDirectorMovieCount(String directorName) {
        String query = String.format(
                "SELECT COUNT(m.Name) AS MovieCount " +
                        "FROM Movie m JOIN Director d ON m.DirectorId = d.Id " +
                        "WHERE d.Name = '%s'", directorName);

        Dataset<Row> result = sparkSession.sql(query);
        return new QueryResultDTO<>(result.first().getLong(0), System.currentTimeMillis());
    }

    // 5. 经常一起合作的导演和演员
    public QueryResultDTO<List<List<String>>> getDirectorsWithActors() {
        String query =
                "SELECT d.Name AS DirectorName, a.Name AS ActorName, COUNT(m) AS MovieCount " +
                        "FROM Director d JOIN Movie m ON d.Id = m.DirectorId " +
                        "JOIN Actor a ON m.ActorId = a.Id " +
                        "GROUP BY d.Name, a.Name " +
                        "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> directorActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), row.getString(1), String.valueOf(row.getLong(2))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(directorActorPairs, System.currentTimeMillis());
    }
}