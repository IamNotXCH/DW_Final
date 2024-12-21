package com.example.java.Controller;

import com.example.java.Mapper.MovieMapper;
import com.example.java.DTO.MovieVersionCountDTO;
import com.example.java.DTO.MovieWithGradeDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class MovieController {

    @Autowired
    private MovieMapper movieMapper;

    // 查询电影名称、评分和版本数目，按电影名称分组
    @GetMapping("/movies/versions")
    public Map<String, Object> countMoviesGroupedByNameWithVersionCount(@Param("name") String name) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieVersionCountDTO> result = movieMapper.countMoviesGroupedByNameWithVersionCount(name);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某类别的所有电影
    @GetMapping("/movies/{typeName}")
    public Map<String, Object> findMoviesByType(@PathVariable("typeName") String typeName) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = movieMapper.findMoviesByType(typeName);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询评分高于 4.5 的电影
    @GetMapping("/movies/high-grade")
    public Map<String, Object> findMoviesWithHighGrade(@Param("score") double score,@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = movieMapper.findMoviesWithHighGrade(score,limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询用户评价中有正面评价的电影名称
    @GetMapping("/movies/positive-reviews")
    public Map<String, Object> findMoviesWithPositiveReviews(@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<String> result = movieMapper.findMoviesWithPositiveReviews(limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查找有多个版本的电影
    @GetMapping("/movies/multiple-versions")
    public Map<String, Object> findMoviesWithMultipleVersions(@Param("limit") int limit) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieVersionCountDTO> result = movieMapper.findMoviesWithMultipleVersions(limit);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }
}
