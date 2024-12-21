package com.example.java.Controller;

import com.example.java.Entity.ActorDirectorCooperation;
import com.example.java.Entity.MovieRough;
import com.example.java.Mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PersonController {

    @Autowired
    private PersonMapper personMapper;


    // 输入导演名称，返回导演执导过的电影
    @GetMapping("/movies/byDirector")
    public Map<String, Object> getMoviesByDirector(@RequestParam String directorName) {
        long startTime = System.currentTimeMillis();
        // 查询导演执导的电影信息
        List<MovieRough> movies = personMapper.findMoviesByDirector(directorName);
        long endTime = System.currentTimeMillis();

        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");
        return response;
    }

    // 输入演员名称，返回演员参演过的电影
    @GetMapping("/movies/byActor")
    public Map<String, Object> getMoviesByActor(@RequestParam String actorName) {
        long startTime = System.currentTimeMillis();
        // 查询演员参演的电影信息
        List<MovieRough> movies = personMapper.findMoviesByActor(actorName);
        long endTime = System.currentTimeMillis();

        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("queryTime", queryTimeMs + "ms");
        return response;
    }

    // 查询某个导演合作的演员名称和合作次数，并从高到低排序
    @GetMapping("/actors-by-director")
    public ResponseEntity<Map<String, Object>> getActorsByDirector(@RequestParam String directorName) {
        long startTime = System.currentTimeMillis();
        // 查询该导演合作的演员及合作次数
        List<ActorDirectorCooperation> actors = personMapper.findCooperatingActorsByDirector(directorName);
        long endTime = System.currentTimeMillis();

        int queryTimeMs = (int) (endTime - startTime);

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("actors", actors);
        response.put("queryTime", queryTimeMs + "ms");

        return ResponseEntity.ok(response);
    }
}
