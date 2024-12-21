package com.example.java.Controller;

import com.example.java.Mapper.MovieTypeMapper;
import com.example.java.Entity.MovieRough;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieTypeController {

    @Autowired
    private MovieTypeMapper movieTypeMapper;


    // 输入电影类型名称，返回该类型的所有电影
    @GetMapping("/movies/byType")
    public Map<String, Object> getMoviesByType(@RequestParam String typeName) {
        long startTime = System.currentTimeMillis();

        // 查询该类型的所有电影
        List<MovieRough> movies = movieTypeMapper.findMoviesByType(typeName);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);


        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }
}
