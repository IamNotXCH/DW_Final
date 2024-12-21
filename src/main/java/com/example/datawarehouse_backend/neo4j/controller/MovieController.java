package com.example.datawarehouse_backend.neo4j.controller;

import org.springframework.web.bind.annotation.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    /**
     * 某类别的电影总数
     * @param categoryName 电影类别名称
     * @return 某个类别的电影数量
     */
    @GetMapping(path = "/movie-count-by-category", produces = "application/json")
    public HashMap<String, Object> getMovieCountByCategory(@RequestParam String categoryName) {
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影所属类别统计该类别的电影总数
            String query = "MATCH (m:Movie)-[:BELONGS_TO_TYPE]->(t:Type {Name: $categoryName}) " +
                    "RETURN COUNT(m) AS MovieCount";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 categoryName 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("categoryName", categoryName);  // 用于传递类别名称参数
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 获取结果
            if (result.hasNext()) {
                response.put("MovieCount", result.next().get("MovieCount").asLong());
            } else {
                response.put("MovieCount", 0L);  // 如果没有结果，返回 0
            }

            return response;
        }
    }

    /**
     * 获取用户评分高于指定值的电影
     * @param gradeThreshold 评分阈值
     * @return 用户评分高于指定值的电影及其评分
     */
    @GetMapping(path = "/movies-above-grade", produces = "application/json")
    public HashMap<String, Object> getMoviesAboveGrade(@RequestParam float gradeThreshold) {
        try (Session session = driver.session()) {
            // Cypher 查询：获取用户评分高于指定值的电影
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE toFloat(v.Grade) > $gradeThreshold " +
                    "RETURN Distinct m.Name, v.Grade";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 gradeThreshold 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("gradeThreshold", gradeThreshold);  // 用于传递评分阈值参数
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 存储结果
            List<HashMap<String, Object>> moviesList = result.list(record -> {
                HashMap<String, Object> movieMap = new HashMap<>();
                movieMap.put("MovieName", record.get("m.Name").asString());

                // 处理评分字段，确保它是一个有效的数字
                String gradeStr = record.get("v.Grade").asString();
                double grade = 0;
                try {
                    grade = Double.parseDouble(gradeStr);  // 直接使用 double 处理评分
                } catch (NumberFormatException e) {
                    // 如果转换失败，可以选择日志记录或者设置一个默认值
                    grade = 0;
                }
                movieMap.put("Grade", grade);

                return movieMap;
            });

            // 将结果放入响应中
            response.put("moviesAboveGrade", moviesList);
            return response;
        }
    }

    /**
     * 获取用户评价中有正面评价的电影
     * @return 有正面评价的电影名称
     */
    @GetMapping(path = "/movies-with-positive-reviews", produces = "application/json")
    public HashMap<String, Object> getMoviesWithPositiveReviews() {
        try (Session session = driver.session()) {
            // Cypher 查询：获取用户评价中有正面评价的电影
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version)-[:IS_REVIEWED_BY]->(r:Review) " +
                    "WHERE r.score >= 4 AND r.is_positive = true " +
                    "RETURN DISTINCT m.Name AS MovieName";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询
            Result result = session.run(query);

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 存储结果
            List<HashMap<String, Object>> movieNames = result.list(record -> {
                HashMap<String, Object> movieMap = new HashMap<>();
                movieMap.put("MovieName", record.get("MovieName").asString());
                return movieMap;
            });

            // 将结果放入响应中
            response.put("moviesWithPositiveReviews", movieNames);
            return response;
        }
    }

    /**
     * 查询某类别的电影中，与最多演员组合合作的导演及其合作的电影总数
     * @param category 类别名称（如：Drama）
     * @return 导演与演员组合及其合作的电影总数
     */
    @GetMapping(path = "/directors-with-most-actor-collaborations", produces = "application/json")
    public Map<String, Object> getDirectorsWithMostActorCollaborations(@RequestParam String category) {
        // Cypher 查询：获取指定类别的电影中，与最多演员组合合作的导演及其合作的电影总数
        String query = "MATCH (t:Type {Name: $category})<-[:BELONGS_TO_TYPE]-(m:Movie) " +
                "MATCH (m)-[:HAS_DIRECTOR]->(d:Director), " +
                "(m)-[:HAS_ACTOR]->(a1:Actor), " +
                "(m)-[:HAS_ACTOR]->(a2:Actor) " +
                "WHERE id(a1) < id(a2) " + // 确保每对演员组合唯一
                "RETURN d.Name AS DirectorName, " +
                "[a1.Name, a2.Name] AS ActorPair, " +
                "COUNT(m) AS CollaborationCount " +
                "ORDER BY CollaborationCount DESC " +
                "LIMIT 5";

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        try (Session session = driver.session()) {
            // 执行查询，并传入类别参数
            Result result = session.run(query, Map.of("category", category));

            // 存储查询结果
            List<Map<String, Object>> collaborationList = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                Map<String, Object> collaborationMap = new HashMap<>();
                collaborationMap.put("DirectorName", record.get("DirectorName").asString());
                collaborationMap.put("ActorPair", record.get("ActorPair").asList(value -> value.asString()));
                collaborationMap.put("CollaborationCount", record.get("CollaborationCount").asInt());
                collaborationList.add(collaborationMap);
            }

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("collaborations", collaborationList);
            response.put("executionTime", endTime - startTime); // 返回查询时间

            return response;
        }
    }

    /**
     * 查询某演员组合中，参与正面评价最多的某类别电影
     * @param category 类别名称（如：Action）
     * @return 最多参与正面评价的演员组合及其电影数量
     */
    @GetMapping(path = "/movies-with-positive-reviews-by-actor-pair", produces = "application/json")
    public Map<String, Object> getMoviesWithPositiveReviewsByActorPair(@RequestParam String category) {
        // Cypher 查询：获取某演员组合参与的正面评价最多的某类别电影
        String query = "MATCH (t:Type {Name: $category})<-[:BELONGS_TO_TYPE]-(m:Movie) " +
                "MATCH (m)-[:HAS_ACTOR]->(a1:Actor), " +
                "(m)-[:HAS_ACTOR]->(a2:Actor), " +
                "(m)-[:HAS_VERSION]->(v:Version)-[:IS_REVIEWED_BY]->(r:Review) " +
                "WHERE id(a1) < id(a2) " + // 确保每对组合唯一
                "AND r.score > 4 AND r.is_positive = true " +
                "RETURN [a1.Name, a2.Name] AS ActorPair, " +
                "COUNT(DISTINCT m) AS PositiveMovieCount " +
                "ORDER BY PositiveMovieCount DESC";

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        try (Session session = driver.session()) {
            // 执行查询，并传入类别参数
            Result result = session.run(query, Map.of("category", category));

            // 存储查询结果
            List<Map<String, Object>> actorPairList = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                Map<String, Object> actorPairMap = new HashMap<>();
                actorPairMap.put("ActorPair", record.get("ActorPair").asList(value -> value.asString()));
                actorPairMap.put("PositiveMovieCount", record.get("PositiveMovieCount").asInt());
                actorPairList.add(actorPairMap);
            }

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("actorPairs", actorPairList);
            response.put("executionTime", endTime - startTime); // 返回查询时间

            return response;
        }
    }

    /**
     * 查询哈利波特系列的电影信息：电影总数、独特电影总数、版本数和独特版本数
     * @return 哈利波特系列的电影相关信息
     */
    @GetMapping(path = "/harry-potter-movie-info", produces = "application/json")
    public Map<String, Object> getHarryPotterMovieInfo() {
        // Cypher 查询：获取哈利波特系列电影的总数、独特电影总数、版本数和独特版本数
        String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                "WHERE m.Name =~ '(?i).*Harry Potter.*' " + // 模糊匹配，包含“Harry Potter”且不区分大小写
                "RETURN COUNT(m.Name) AS movieCount, " +
                "       COUNT(DISTINCT m.Name) AS movieDistinctCount, " +
                "       COUNT(v.Version) AS VersionCount, " +
                "       COUNT(DISTINCT v.Version) AS VersionDistinctCount";

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        try (Session session = driver.session()) {
            // 执行查询
            Result result = session.run(query);

            // 获取查询结果
            Record record = result.single();

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("movieCount", record.get("movieCount").asInt());
            response.put("movieDistinctCount", record.get("movieDistinctCount").asInt());
            response.put("VersionCount", record.get("VersionCount").asInt());
            response.put("VersionDistinctCount", record.get("VersionDistinctCount").asInt());

            // 记录结束时间
            long endTime = System.currentTimeMillis();
            response.put("executionTime", endTime - startTime); // 返回查询时间

            return response;
        }
    }
}