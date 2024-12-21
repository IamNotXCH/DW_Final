package com.example.java.Controller;

import com.example.java.Mapper.MovieTimeMapper;
import com.example.java.DTO.MovieWithGradeDTO;
import com.example.java.DTO.MovieYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieTimeController {

    @Autowired
    private MovieTimeMapper movieTimeMapper;

    // 查询按时间统计的电影数量
    @GetMapping("/movies/time")
    public Map<String, Object> countMoviesByTime() {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieYear> result = movieTimeMapper.countMoviesByTime();

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某年某月有多少电影
    @GetMapping("/movies/{year}/{month}")
    public Map<String, Object> countMoviesByYearAndMonth(@PathVariable("year") Integer year,
                                                         @PathVariable("month") Integer month) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = movieTimeMapper.countMoviesByYearAndMonth(year, month);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某年某季度有多少电影
    @GetMapping("/movies/{year}/season/{season}")
    public Map<String, Object> countMoviesByYearAndSeason(@PathVariable("year") Integer year,
                                                          @PathVariable("season") Integer season) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = movieTimeMapper.countMoviesByYearAndSeason(year, season);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }
}
