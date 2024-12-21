package com.example.java.Controller;

import com.example.java.Entity.Category;
import com.example.java.Entity.CategoryTopMovies;
import com.example.java.Entity.MovieYear;
import com.example.java.Mapper.MovieCombineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MovieCombineController {

    @Autowired
    private MovieCombineMapper movieCombineMapper;

    // 查找xx年以来xx指导的所有xx风格的电影
    @GetMapping("/movies/director/type")
    public Map<String, Object> getMoviesByDirectorAndType(@RequestParam String directorName,
                                                          @RequestParam String typeName,
                                                          @RequestParam int year) {
        long startTime = System.currentTimeMillis();

        // 查询指定导演和电影类型的电影
        List<MovieYear> movieYears = movieCombineMapper.findMoviesByDirectorAndType(directorName, typeName, year);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movieYears);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    @GetMapping("/top-category")
    public Map<String, Object> getTopCategory(@RequestParam int year) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        // 查询最常见的电影类别以及该类别下最受欢迎的前20部电影
        List<CategoryTopMovies> categoryTopMovies = movieCombineMapper.getTopMovieOnMostFrequentStyle(year);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        int queryTimeMs = (int) (endTime - startTime); // 计算查询耗时

        // 组装响应
        Map<String, Object> response = new HashMap<>();
        response.put("topCategories", categoryTopMovies);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 查找xx年中，评分大于xx的电影
    @GetMapping("/movies/year/grade")
    public Map<String, Object> getMoviesByYearAndGrade(@RequestParam int year,
                                                       @RequestParam double minGrade) {
        long startTime = System.currentTimeMillis();

        // 查询评分大于指定分数的电影
        List<MovieYear> movieYears = movieCombineMapper.findMoviesByYearAndGrade(year, minGrade);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movieYears);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }
}
