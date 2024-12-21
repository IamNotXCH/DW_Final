package com.example.java.Controller;

import com.example.java.Entity.MovieCountByMonth;
import com.example.java.Mapper.MovieTimeMapper;
import com.example.java.Entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieTimeController {

    @Autowired
    private MovieTimeMapper movieTimeMapper;

    // 输入年份，返回电影名称、评分、月份
    @GetMapping("/byYear")
    public Map<String, Object> getMoviesByYear(@RequestParam Integer year) {
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieTimeMapper.findMoviesByYear(year);
        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", (endTime - startTime) + "ms");
        return response;
    }

    // 输入年份和月份，返回电影名称、评分、月份
    @GetMapping("/byYearAndMonth")
    public Map<String, Object> getMoviesByYearAndMonth(@RequestParam Integer year, @RequestParam Integer month) {
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieTimeMapper.findMoviesByYearAndMonth(year, month);
        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", (endTime - startTime) + "ms");
        return response;
    }

    // 输入年份和季度，返回电影名称、评分、月份
    @GetMapping("/byYearAndSeason")
    public Map<String, Object> getMoviesByYearAndSeason(@RequestParam Integer year, @RequestParam Integer season) {
        long startTime = System.currentTimeMillis();
        List<Movie> movies = movieTimeMapper.findMoviesByYearAndSeason(year, season);
        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", (endTime - startTime) + "ms");
        return response;
    }

    // 输入年份，返回该年发行的电影数量
    @GetMapping("/countByYear")
    public Map<String, Object> countMoviesByYear(@RequestParam Integer year) {
        long startTime = System.currentTimeMillis();
        Integer count = movieTimeMapper.countMoviesByYear(year);
        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("queryTime", (endTime - startTime) + "ms");
        return response;
    }

    // 输入年份，返回该年每月发行的电影数量
    @GetMapping("/countByYearAndMonth")
    public Map<String, Object> countMoviesByYearAndMonth(@RequestParam Integer year) {
        long startTime = System.currentTimeMillis();
        List<MovieCountByMonth> count = movieTimeMapper.countMoviesByYearAndMonth(year);
        long endTime = System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("queryTime", (endTime - startTime) + "ms");
        return response;
    }
}
