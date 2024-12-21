package com.example.java.Controller;

import com.example.java.Entity.MovieYear;
import com.example.java.Mapper.MovieReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieReviewController {

    @Autowired
    private MovieReviewMapper movieReviewMapper;

    // 查询评分大于等于某个分数的电影，返回电影名称、评分和发行年份
    @GetMapping("/movies/byScore")
    public Map<String, Object> getMoviesByScore(@RequestParam double score) {
        long startTime = System.currentTimeMillis();  // 记录开始时间

        // 查询评分大于等于某个分数的电影
        List<MovieYear> movies = movieReviewMapper.findMoviesByScore(score);

        long endTime = System.currentTimeMillis();  // 记录结束时间
        int queryTimeMs = (int) (endTime - startTime);  // 计算查询时间（毫秒）

        // 创建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);  // 电影数据
        response.put("queryTime", queryTimeMs + "ms");  // 查询时间

        return response;  // 返回查询结果和查询时间
    }

    // 查询至少有一条正面评价的电影，返回电影名称、评分和发行年份
    @GetMapping("/movies/withPositiveReview")
    public Map<String, Object> getMoviesWithPositiveReview() {
        long startTime = System.currentTimeMillis();

        // 查询至少有一条正面评价的电影
        List<MovieYear> movies = movieReviewMapper.findMoviesWithPositiveReview();

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 查询所有评价都是正面评价的电影，返回电影名称、评分和发行年份
    @GetMapping("/movies/allPositiveReview")
    public Map<String, Object> getMoviesWithAllPositiveReview() {
        long startTime = System.currentTimeMillis();

        // 查询所有评价都是正面评价的电影
        List<MovieYear> movies = movieReviewMapper.findMoviesWithAllPositiveReview();

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 查询至少有一条负面评价的电影，返回电影名称、评分和发行年份
    @GetMapping("/movies/withNegativeReview")
    public Map<String, Object> getMoviesWithNegativeReview() {
        long startTime = System.currentTimeMillis();

        // 查询至少有一条负面评价的电影
        List<MovieYear> movies = movieReviewMapper.findMoviesWithNegativeReview();

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 查询好评数最多的电影，返回电影名称、评分和发行年份
    @GetMapping("/movies/mostPositiveReviews")
    public Map<String, Object> getMovieWithMostPositiveReviews() {
        long startTime = System.currentTimeMillis();

        // 查询好评数最多的电影
        MovieYear movie = movieReviewMapper.findMovieWithMostPositiveReviews();

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movie", movie);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }
}
