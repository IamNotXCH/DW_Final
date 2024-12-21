package com.example.datawarehouse_backend.neo4j.controller;

import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class PersonController {

    private final Driver driver;

    public PersonController() {
        driver = GraphDatabase.driver("bolt://139.224.137.93:7687",
                AuthTokens.basic("neo4j", "Dw123456"));
    }

    @GetMapping(path = "/director-movie-count", produces = "application/json")
    public HashMap<String, Object> getDirectorMovieCount(@RequestParam String directorName) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：根据导演的名称统计其执导的电影数量
            String query = "MATCH (m:Movie)-[:HAS_DIRECTOR]->(d:Director {Name: $directorName}) " +
                    "RETURN count(m.Name) AS MovieCount";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入导演的名字参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("directorName", directorName);  // 用于传递导演名称参数，如 'Tony'
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

    @GetMapping(path = "/actor-movie-count", produces = "application/json")
    public HashMap<String, Object> getActorMovieCount(@RequestParam String actorName) {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：根据演员名称统计参演的电影数量，并列出前五部电影
            String query = "MATCH (a:Actor {Name: $actorName})<-[:HAS_ACTOR]-(m:Movie) " +
                    "RETURN a.Name AS ActorName, COUNT(m) AS MovieCount, COLLECT(m.Name)[..5] AS TopMovies";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询，并传入演员名称参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("actorName", actorName);  // 用于传递演员名称参数，如 'Roy Lichtenstein'
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 计算执行时间
            response.put("time", endTime - startTime); // 执行时间（单位：毫秒）

            // 获取查询结果
            if (result.hasNext()) {
                // 提取查询结果
                var record = result.next();
                response.put("ActorName", record.get("ActorName").asString());
                response.put("MovieCount", record.get("MovieCount").asLong());
                response.put("TopMovies", record.get("TopMovies").asList(value -> value.asString()));
                response.put("status", "success");  // 查询成功
            } else {
                response.put("ActorName", actorName);
                response.put("MovieCount", 0L);  // 如果没有结果，返回 0
                response.put("TopMovies", List.of());  // 如果没有结果，返回空列表
                response.put("status", "no_data");  // 没有数据
            }
        } catch (Exception e) {
            response.put("status", "error");  // 处理异常情况
            response.put("message", e.getMessage());
        }
        return response;
    }

    @GetMapping(path = "/actor-collaboration", produces = "application/json")
    public HashMap<String, Object> getActorCollaboration() {
        HashMap<String, Object> response = new HashMap<>();
        try (Session session = driver.session()) {
            // Cypher 查询：查找常合作的演员组合，并按合作次数排序
            String query = "MATCH (a1:Actor)<-[:HAS_ACTOR]-(m:Movie)-[:HAS_ACTOR]->(a2:Actor) " +
                    "WHERE a1 <> a2 " +
                    "RETURN a1.Name AS Actor1, a2.Name AS Actor2, COUNT(m) AS MovieCount " +
                    "ORDER BY MovieCount DESC " +
                    "LIMIT 100";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询
            Result result = session.run(query);

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 计算执行时间
            response.put("time", endTime - startTime); // 执行时间（单位：毫秒）

            // 存储结果
            List<HashMap<String, Object>> collaborations = result.list(record -> {
                HashMap<String, Object> collaboration = new HashMap<>();
                collaboration.put("Actor1", record.get("Actor1").asString());
                collaboration.put("Actor2", record.get("Actor2").asString());
                collaboration.put("MovieCount", record.get("MovieCount").asLong());
                return collaboration;
            });

            // 返回查询结果
            response.put("Collaborations", collaborations);
            response.put("status", "success");  // 查询成功
        } catch (Exception e) {
            response.put("status", "error");  // 处理异常情况
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * 经常一起合作的导演和演员有哪些
     * @return 导演和演员的合作情况，包括合作次数
     */
    @GetMapping(path = "/director-actor-collaborations", produces = "application/json")
    public HashMap<String, Object> getDirectorActorCollaborations() {
        try (Session session = driver.session()) {
            // Cypher 查询：根据导演和演员的合作情况，统计他们一起合作的电影数量
            String query = "MATCH (d:Director)<-[:HAS_DIRECTOR]-(m:Movie)-[:HAS_ACTOR]->(a:Actor) " +
                    "RETURN d.Name AS DirectorName, a.Name AS ActorName, COUNT(m) AS MovieCount " +
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

            // 将查询结果转换为 Map
            List<HashMap<String, Object>> collaborationsList = result.list(record -> {
                HashMap<String, Object> collaborationMap = new HashMap<>();
                collaborationMap.put("directorName", record.get("DirectorName").asString());
                collaborationMap.put("actorName", record.get("ActorName").asString());
                collaborationMap.put("movieCount", record.get("MovieCount").asLong());
                return collaborationMap;
            });

            // 将结果放入响应中
            response.put("collaborations", collaborationsList);
            return response;
        }
    }

    /**
     * 获取最受关注的演员组合（评论最多）的列表
     * @param type 电影类型（如 "Action"）
     * @return 最受关注的演员组合及评论总数
     */
    @GetMapping(path = "/most-attended-actor-pair", produces = "application/json")
    public HashMap<String, Object> getMostAttendedActorPair(@RequestParam String type) {
        try (Session session = driver.session()) {
            // Cypher 查询：获取最受关注的演员组合及评论总数
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

            // 执行查询并传入 type 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("type", type);  // 用于传递电影类型，如 "Action"
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 存储结果
            List<HashMap<String, Object>> actorPairs = result.list(record -> {
                HashMap<String, Object> pairMap = new HashMap<>();
                pairMap.put("ActorPair", record.get("ActorPair").asList(Value::asString)); // 获取演员组合
                pairMap.put("TotalComments", record.get("TotalComments").asLong()); // 获取评论总数
                return pairMap;
            });

            // 将结果放入响应中
            response.put("mostAttendedActorPairs", actorPairs);
            return response;
        }
    }

    /**
     * 查询某类型电影中评分最高的演员组合及其参与的电影数量
     * @param type 电影类型（如 "Horror"）
     * @return 评分最高的演员组合及其参与的电影数量
     */
    @GetMapping(path = "/highest-rated-actor-pair", produces = "application/json")
    public HashMap<String, Object> getHighestRatedActorPair(@RequestParam String type) {
        try (Session session = driver.session()) {
            // Cypher 查询：获取评分最高的演员组合及其参与的电影数量
            String query = "MATCH (t:Type {Name: $type})<-[:BELONGS_TO_TYPE]-(m:Movie)-[:HAS_VERSION]->(v:Version), " +
                    "(m)-[:HAS_ACTOR]->(a1:Actor), " +
                    "(m)-[:HAS_ACTOR]->(a2:Actor) " +
                    "WHERE id(a1) < id(a2) " + // 确保每对组合唯一
                    "RETURN " +
                    "[a1.Name, a2.Name] AS ActorPair, " +
                    "AVG(toFloat(v.Grade)) AS AverageScore, " +
                    "COUNT(m) AS MovieCount " +
                    "ORDER BY AverageScore DESC, MovieCount DESC";

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行查询并传入 type 参数
            Result result = session.run(query, new HashMap<String, Object>() {{
                put("type", type);  // 用于传递电影类型，如 "Horror"
            }});

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 构建响应数据
            HashMap<String, Object> response = new HashMap<>();
            response.put("time", endTime - startTime);

            // 存储结果
            List<HashMap<String, Object>> actorPairs = result.list(record -> {
                HashMap<String, Object> pairMap = new HashMap<>();
                pairMap.put("ActorPair", record.get("ActorPair").asList(Value::asString)); // 获取演员组合
                pairMap.put("AverageScore", record.get("AverageScore").asDouble()); // 获取平均评分
                pairMap.put("MovieCount", record.get("MovieCount").asLong()); // 获取电影数量
                return pairMap;
            });

            // 将结果放入响应中
            response.put("highestRatedActorPairs", actorPairs);
            return response;
        }
    }


}