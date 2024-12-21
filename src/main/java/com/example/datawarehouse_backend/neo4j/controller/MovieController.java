package com.example.datawarehouse_backend.neo4j.controller;

import org.springframework.web.bind.annotation.*;
import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/neo4j/movie")
public class MovieController {

    private final Driver driver;

    public MovieController() {
        driver = GraphDatabase.driver("bolt://139.224.137.93:7687",
                AuthTokens.basic("neo4j", "Dw123456"));
    }

    /**
     * 根据电影发布年份统计电影数量
     * @return 电影发布年份和数量的统计
     */
    @GetMapping(path = "/movie-count-by-year", produces = "application/json")
    public HashMap<String, Object> getMovieCountByYear() {
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影发布版本的 ReleaseTime 统计每年发布的电影数量
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "RETURN v.ReleaseTime AS Year, COUNT(m) AS MovieCount " +
                    "ORDER BY Year";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询
            Result result = session.run(query);

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 将查询结果转换为 List
            List<HashMap<String, Object>> movieCountList = result.list(record -> {
                HashMap<String, Object> countMap = new HashMap<>();
                countMap.put("year", record.get("Year").asString());  // 电影发布年份
                countMap.put("movieCount", record.get("MovieCount").asLong());  // 电影数量
                return countMap;
            });

            // 限制返回前100条
            if (movieCountList.size() > 100) {
                movieCountList = movieCountList.subList(0, 100);
            }

            // 将结果放入响应中
            response.put("movieCountByYear", movieCountList);
            return response;
        }
    }

    @GetMapping(path = "/movie-count-by-month", produces = "application/json")
    public HashMap<String, Object> getMovieCountByMonth(@RequestParam String yearMonth) {
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影发布版本的 ReleaseTime 统计每月发布的电影数量
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE v.ReleaseTime STARTS WITH $yearMonth " +
                    "RETURN COUNT(DISTINCT m) AS MovieCount";
            // 记录开始时间
            long startTime = System.currentTimeMillis();
            // 执行查询，并传入 yearMonth 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("yearMonth", yearMonth);  // 用于传递 yearMonth 参数，如 '2024/12'
            }});
            // 记录结束时间
            long endTime = System.currentTimeMillis();
            // 获取结果
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);
            if (result.hasNext()) {
                response.put("MovieCount", result.next().get("MovieCount").asLong());
            } else {
                response.put("MovieCount", 0L);  // 如果没有结果，返回 0
            }

            return response;
        }
    }

    @GetMapping(path = "/movie-count-by-quarter", produces = "application/json")
    public HashMap<String, Object> getMovieCountByQuarter(@RequestParam String year) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影发布版本的 ReleaseTime 统计特定年份和季度发布的电影数量
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE v.ReleaseTime STARTS WITH $year " +  // 匹配年份
                    "AND toInteger(split(v.ReleaseTime, '/')[1]) IN [1, 2, 3] " +  // 匹配季度
                    "RETURN COUNT(DISTINCT m) AS MovieCount";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 year 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("year", year);  // 用于传递 year 参数，如 '2001'
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 计算执行时间
            response.put("time", endTime - startTime); // 执行时间（单位：毫秒）

            // 获取结果
            if (result.hasNext()) {
                response.put("MovieCount", result.next().get("MovieCount").asLong());
                response.put("status", "success");  // 查询成功
            } else {
                response.put("MovieCount", 0L);  // 如果没有结果，返回 0
                response.put("status", "no_data");  // 没有数据
            }
        } catch (Exception e) {
            response.put("status", "error");  // 处理异常情况
            response.put("message", e.getMessage());
        }
        return response;
    }

    @GetMapping(path = "/movie-version-count", produces = "application/json")
    public HashMap<String, Object> getMovieVersionCount(@RequestParam String movieName) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影名称统计电影的版本数量
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE m.Name =~ '(?i).*' + $movieName + '.*' " +  // 模糊匹配电影名称，不区分大小写
                    "RETURN COUNT(distinct v.Version) AS VersionCount";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 movieName 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("movieName", movieName);  // 用于传递 movieName 参数，如 'Roxy Hunter'
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 计算执行时间
            response.put("time", endTime - startTime); // 执行时间（单位：毫秒）

            // 获取结果
            if (result.hasNext()) {
                response.put("VersionCount", result.next().get("VersionCount").asLong());
                response.put("status", "success");  // 查询成功
            } else {
                response.put("VersionCount", 0L);  // 如果没有结果，返回 0
                response.put("status", "no_data");  // 没有数据
            }
        } catch (Exception e) {
            response.put("status", "error");  // 处理异常情况
            response.put("message", e.getMessage());
        }
        return response;
    }
}