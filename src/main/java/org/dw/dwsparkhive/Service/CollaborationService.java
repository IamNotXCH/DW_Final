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

    // 5. 常合作的演员组合
    public QueryResultDTO<List<List<String>>> getFrequentActorPairs() {
        String query = "SELECT a1.Name AS Actor1, a2.Name AS Actor2, COUNT(m) AS MovieCount " +
                "FROM Actor a1 JOIN Movie m ON a1.Id = m.ActorId " +
                "JOIN Actor a2 ON m.ActorId = a2.Id " +
                "WHERE a1.Id <> a2.Id " +
                "GROUP BY a1.Name, a2.Name " +
                "ORDER BY MovieCount DESC";

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> actorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), row.getString(1), String.valueOf(row.getLong(2))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(actorPairs, System.currentTimeMillis());
    }

    // 8. 如果要拍一部XXX类型的电影，最受关注的演员组合
    public QueryResultDTO<List<List<String>>> getPopularActorPairsByType(String typeName) {
        String query = String.format(
                "SELECT a1.Name AS Actor1, a2.Name AS Actor2, SUM(CASE WHEN v.Comments = 'Unknown' THEN 0 ELSE CAST(v.Comments AS INT) END) AS TotalComments " +
                        "FROM Type t JOIN Movie m ON t.Id = m.TypeId " +
                        "JOIN Version v ON m.Id = v.MovieId " +
                        "JOIN Actor a1 ON m.ActorId = a1.Id " +
                        "JOIN Actor a2 ON m.ActorId = a2.Id " +
                        "WHERE t.Name = '%s' AND a1.Id <> a2.Id " +
                        "GROUP BY a1.Name, a2.Name " +
                        "ORDER BY TotalComments DESC", typeName);

        Dataset<Row> result = sparkSession.sql(query);
        List<List<String>> popularActorPairs = result.collectAsList().stream()
                .map(row -> Arrays.asList(row.getString(0), row.getString(1), String.valueOf(row.getLong(2))))
                .collect(Collectors.toList());

        return new QueryResultDTO<>(popularActorPairs, System.currentTimeMillis());
    }
}