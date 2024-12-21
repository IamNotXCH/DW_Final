package com.example.java.Controller;

import com.example.java.DTO.*;
import com.example.java.Mapper.RelationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "演员与导演合作 API", description = "演员与导演合作数据的相关接口")
@RestController
public class RelationController {

    @Autowired
    private RelationMapper relationMapper;

    // 查询常合作的演员组合和他们的合作电影数量
    @Operation(description = "查询最常合作的演员组合及其合作的电影数量")
    @GetMapping("/actors/collaboration")
    public Map<String, Object> findMostCollaborativeActorPairs(@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<ActorPairDTO> result = relationMapper.findMostCollaborativeActorPairs(limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询经常一起合作的导演和演员，以及他们合作的电影数量
    @Operation(description = "查询最常合作的导演与演员及其合作的电影数量")
    @GetMapping("/directors/actors/collaboration")
    public Map<String, Object> findMostCollaborativeDirectorsAndActors(@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<DirectorActorCooperationDTO> result = relationMapper.findMostCollaborativeDirectorsAndActors(limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查找最受欢迎的演员组合
    @Operation(description = "根据评论数量查找最受欢迎的演员组合")
    @GetMapping("/actors/popular/collaboration")
    public Map<String, Object> findMostPopularActorPairs(@Param("type") String type,@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<ActorPairWithCommentsDTO> result = relationMapper.findMostPopularActorPairs(type,limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某类别的电影中，与最多演员组合合作的导演及其合作的电影总数
    @Operation(description = "查询某类别电影中与最多演员组合合作的导演及其合作的电影数量")
    @GetMapping("/directors/{type}/collaboration")
    public Map<String, Object> findTopDirectorsWithActorCollaboration(@PathVariable("type") String type,@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<DirectorActorCooperationDTO> result = relationMapper.findTopDirectorsWithActorCollaboration(type,limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某演员组合中，参与正面评价最多的某类别电影
    @Operation(description = "查询某演员组合中，参与正面评价最多的某类别电影")
    @GetMapping("/actors/{type}/positive-reviews")
    public Map<String, Object> findTopActorPairsWithPositiveReviews(@PathVariable("type") String type,@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<ActorMovieCountDTO> result = relationMapper.findTopActorPairsWithPositiveReviews(type,limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某类型电影中评分最高的演员组合及其参与的电影数量
    @Operation(description = "查询某类型电影中评分最高的演员组合及其参与的电影数量")
    @GetMapping("/actors/{type}/highest-score")
    public Map<String, Object> findTopActorPairsWithHighestScore(@PathVariable("type") String type,@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<ActorGradeCountDTO> result = relationMapper.findTopActorPairsWithHighestScore(type,limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }
}
