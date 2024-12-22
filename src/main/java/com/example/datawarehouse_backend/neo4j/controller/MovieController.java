package com.example.datawarehouse_backend.neo4j.controller;

import org.springframework.web.bind.annotation.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.*;

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
                    "WITH m, v, substring(v.ReleaseTime, 0, 4) AS Year " +
                    "RETURN Year AS ReleaseYear, COUNT(DISTINCT m) AS MovieCount " +
                    "ORDER BY MovieCount DESC";
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
                countMap.put("year", record.get("ReleaseYear").asString());  // 电影发布年份
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
            // Cypher 查询：根据电影发布版本的 ReleaseTime 获取电影名称和评分，并去重电影名称
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE v.ReleaseTime STARTS WITH $yearMonth " +
                    "WITH m.Name AS MovieName, COLLECT(DISTINCT v.Grade) AS Grades, COLLECT(DISTINCT v.ReleaseTime) AS Time " +
                    "RETURN MovieName, HEAD(Grades) AS MovieGrade, HEAD(Time) AS MovieReleaseTime";

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

            // 存储去重后的电影名称和对应的评分
            List<HashMap<String, Object>> movies = result.list(record -> {
                HashMap<String, Object> movieMap = new HashMap<>();
                movieMap.put("MovieName", record.get("MovieName").asString());
                movieMap.put("MovieGrade", record.get("MovieGrade").asString());
                return movieMap;
            });

            // 将电影列表放入响应中
            response.put("movies", movies);

            // 统计去重后的电影数量
            long movieCount = movies.size();  // 因为返回的是去重后的电影名称，所以计算大小即为电影数量
            response.put("MovieCount", movieCount);

            return response;
        }
    }

    @GetMapping(path = "/movie-details-by-quarter", produces = "application/json")
    public HashMap<String, Object> getMovieDetailsByQuarter(@RequestParam String year, @RequestParam int quarter) {
        try (Session session = driver.session()) {
            // 根据季度选择相应的月份
            List<Integer> months = new ArrayList<>();
            switch (quarter) {
                case 1:
                    months = Arrays.asList(1, 2, 3);  // 第一季度：1, 2, 3月
                    break;
                case 2:
                    months = Arrays.asList(4, 5, 6);  // 第二季度：4, 5, 6月
                    break;
                case 3:
                    months = Arrays.asList(7, 8, 9);  // 第三季度：7, 8, 9月
                    break;
                case 4:
                    months = Arrays.asList(10, 11, 12); // 第四季度：10, 11, 12月
                    break;
                default:
                    throw new IllegalArgumentException("Invalid quarter: " + quarter); // 无效季度
            }

            // 构造 Cypher 查询
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE v.ReleaseTime STARTS WITH $year " + // 确保年份匹配
                    "AND toInteger(split(v.ReleaseTime, '/')[1]) IN $months " + // 只选择指定季度的月份
                    "WITH m.Name AS MovieName, COLLECT(DISTINCT v.Grade) AS Grades, COLLECT(DISTINCT v.ReleaseTime) AS Time " +
                    "RETURN MovieName, HEAD(Grades) AS MovieGrade, HEAD(Time) AS MovieReleaseTime";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 year 和 months 参数
            List<Integer> finalMonths = months;
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("year", year);  // 用于传递年份参数，如 '2001'
                put("months", finalMonths);  // 用于传递选择的月份列表
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 获取结果
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 存储查询结果
            List<HashMap<String, Object>> movies = result.list(record -> {
                HashMap<String, Object> movie = new HashMap<>();
                movie.put("MovieName", record.get("MovieName").asString());
                movie.put("MovieGrade", record.get("MovieGrade").asString());
                movie.put("MovieReleaseTime", record.get("MovieReleaseTime").asString());
                return movie;
            });

            response.put("movies", movies);
            // 统计去重后的电影数量
            long movieCount = movies.size();  // 因为返回的是去重后的电影名称，所以计算大小即为电影数量
            response.put("MovieCount", movieCount);

            return response;
        }
    }

    @GetMapping(path = "/movie-version-count", produces = "application/json")
    public HashMap<String, Object> getMovieVersionCount(@RequestParam String movieName) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影名称列出所有版本的评分和发行时间
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE m.Name =~ '(?i).*' + $movieName + '.*' " +  // 模糊匹配电影名称，不区分大小写
                    "RETURN m.Name AS MovieName, v.Grade AS MovieGrade, v.ReleaseTime AS MovieReleaseTime " +
                    "ORDER BY v.ReleaseTime";  // 按照版本的 ReleaseTime 排序（可选）

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

            // 获取结果并构建响应
            List<Map<String, Object>> versions = new ArrayList<>();
            int versionCount = 0;  // 版本计数器
            while (result.hasNext()) {
                Record record = result.next();
                Map<String, Object> version = new HashMap<>();
                version.put("MovieName", record.get("MovieName").asString());
                version.put("MovieGrade", record.get("MovieGrade").asString());
                version.put("MovieReleaseTime", record.get("MovieReleaseTime").asString());
                versions.add(version);
                versionCount++;  // 每处理一个版本，计数加 1
            }

            if (!versions.isEmpty()) {
                response.put("VersionCount", versionCount);  // 返回版本数量
                response.put("versions", versions);  // 返回所有版本的列表
                response.put("status", "success");  // 查询成功
            } else {
                response.put("status", "no_data");  // 如果没有数据，返回 no_data
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
    @GetMapping(path = "/movies-by-category", produces = "application/json")
    public HashMap<String, Object> getMoviesByCategory(@RequestParam String categoryName) {
        try (Session session = driver.session()) {
            // Cypher 查询：根据电影所属类别获取该类别的电影名称
            String query = "MATCH (m:Movie)-[:BELONGS_TO_TYPE]->(t:Type {Name: $categoryName}) " +
                    "RETURN m.Name AS MovieName";

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
            response.put("time", endTime - startTime); // 执行时间

            // 存储电影名称列表
            List<String> movieNames = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                movieNames.add(record.get("MovieName").asString());
            }

            // 构建响应数据
            response.put("movies", movieNames);  // 电影名称列表
            response.put("MovieCount", movieNames.size());  // 电影数量

            return response;
        } catch (Exception e) {
            // 异常处理
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return errorResponse;
        }
    }

    /**
     * 获取用户评分高于指定值的电影
     * @param gradeThreshold 评分阈值
     * @return 用户评分高于指定值的电影及其评分
     */
    @GetMapping(path = "/movies-above-grade", produces = "application/json")
    public HashMap<String, Object> getMoviesAboveGrade(@RequestParam float gradeThreshold) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：获取用户评分高于指定值的电影
            String query = "MATCH (m:Movie)-[:HAS_VERSION]->(v:Version) " +
                    "WHERE toFloat(v.Grade) >= $gradeThreshold " +
                    "WITH m.Name AS MovieName, COLLECT(DISTINCT v.Grade) AS Grades, COLLECT(DISTINCT v.ReleaseTime) AS Times " +
                    "RETURN MovieName, HEAD(Grades) AS MovieGrade, HEAD(Times) AS MovieReleaseTime "+
                    "LIMIT 100";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入 gradeThreshold 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("gradeThreshold", gradeThreshold);  // 用于传递评分阈值参数
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            response.put("time", endTime - startTime); // 执行时间

            // 存储结果
            List<HashMap<String, Object>> moviesList = result.list(record -> {
                HashMap<String, Object> movieMap = new HashMap<>();
                movieMap.put("MovieName", record.get("MovieName").asString());
                movieMap.put("MovieGrade", record.get("MovieGrade").asString());
                movieMap.put("MovieReleaseTime", record.get("MovieReleaseTime").asString());
                return movieMap;
            });

            // 将结果放入响应中
            response.put("moviesAboveGrade", moviesList);
            response.put("status", "success"); // 标识查询成功
        } catch (Exception e) {
            // 处理异常
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
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
     * 获取某类型电影中评论最多的演员组合
     * @param type 电影类型（如 "Action"）
     * @return 演员组合及其总评论数
     */
    @GetMapping(path = "/most-commented-actor-pairs", produces = "application/json")
    public HashMap<String, Object> getMostCommentedActorPairs(@RequestParam String type) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：获取某类型电影中评论最多的演员组合
            String query = "MATCH (t:Type {Name: $type})<-[:BELONGS_TO_TYPE]-(m:Movie)-[:HAS_VERSION]->(v:Version), " +
                    "(m)-[:HAS_ACTOR]->(a1:Actor), " +
                    "(m)-[:HAS_ACTOR]->(a2:Actor) " +
                    "WHERE id(a1) < id(a2) " + // 确保每对组合唯一
                    "RETURN " +
                    "[a1.Name, a2.Name] AS ActorPair, " +
                    "SUM(CASE WHEN v.Comments = 'Unknown' THEN 0 ELSE toInteger(v.Comments) END) AS TotalComments " +
                    "ORDER BY TotalComments DESC";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询并传入类型参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("type", type); // 传入电影类型（如 "Action"）
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            response.put("time", endTime - startTime); // 执行时间

            // 存储结果
            List<HashMap<String, Object>> actorPairs = result.list(record -> {
                HashMap<String, Object> pairMap = new HashMap<>();
                pairMap.put("ActorPair", record.get("ActorPair").asList(Value::asString)); // 演员组合
                pairMap.put("TotalComments", record.get("TotalComments").asLong()); // 总评论数
                return pairMap;
            });

            // 将结果放入响应中
            response.put("mostCommentedActorPairs", actorPairs);
            response.put("status", "success"); // 查询成功
        } catch (Exception e) {
            // 处理异常
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
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
                "ORDER BY PositiveMovieCount DESC "+
                "LIMIT 100";

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