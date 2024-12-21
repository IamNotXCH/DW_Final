package com.example.java.Controller;

import com.example.java.DTO.ActorMoviesDTO;
import com.example.java.DTO.MovieWithGradeDTO;
import com.example.java.Mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PersonController {

    @Autowired
    private PersonMapper personMapper;

    // 查询某导演的所有电影
    @GetMapping("/movies/director/{directorName}")
    public Map<String, Object> countMoviesByDirectorName(@PathVariable("directorName") String directorName) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = personMapper.countMoviesByDirectorName(directorName);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }

    // 查询某演员的所有电影
    @GetMapping("/movies/actor/{actorName}")
    public Map<String, Object> countMoviesByActorName(@PathVariable("actorName") String actorName) {
        long startTime = System.currentTimeMillis(); // 记录查询开始时间

        List<MovieWithGradeDTO> result = personMapper.countMoviesByActorName(actorName);

        long endTime = System.currentTimeMillis(); // 记录查询结束时间
        long duration = endTime - startTime; // 计算查询时长

        Map<String, Object> response = new HashMap<>();
        response.put("data", result); // 返回查询结果
        response.put("queryTime", duration + " ms"); // 返回查询时长

        return response;
    }
}
