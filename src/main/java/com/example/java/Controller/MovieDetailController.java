package com.example.java.Controller;

import com.example.java.Entity.MovieDetail;
import com.example.java.Entity.MovieVersion;
import com.example.java.Entity.MovieVersionAndType;
import com.example.java.Mapper.MovieDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieDetailController {

    @Autowired
    private MovieDetailMapper movieDetailMapper;

    // 根据电影名查找电影的演员数量、导演数量、评论数量、评分
    @GetMapping("/movie/details")
    public Map<String, Object> getMovieDetails(@RequestParam String movieName) {
        long startTime = System.currentTimeMillis();

        // 查询电影详情（演员数量、导演数量、评论数量、评分）
        MovieDetail movieDetail = movieDetailMapper.findMovieDetailsByName(movieName);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("movieDetail", movieDetail);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 根据电影名查找电影的演员名单
    @GetMapping("/movie/actors")
    public Map<String, Object> getMovieActors(@RequestParam String movieName) {
        long startTime = System.currentTimeMillis();

        // 查询电影的演员名单
        List<String> actors = movieDetailMapper.findActorsByMovieName(movieName);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("actors", actors);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    // 根据电影名查找电影的导演名单
    @GetMapping("/movie/directors")
    public Map<String, Object> getMovieDirectors(@RequestParam String movieName) {
        long startTime = System.currentTimeMillis();

        // 查询电影的导演名单
        List<String> directors = movieDetailMapper.findDirectorsByMovieName(movieName);

        long endTime = System.currentTimeMillis();
        int queryTimeMs = (int) (endTime - startTime);

        Map<String, Object> response = new HashMap<>();
        response.put("directors", directors);
        response.put("queryTime", queryTimeMs + "ms");

        return response;
    }

    @GetMapping("/movies/versions")
    public Map<String, Object> getMovieVersions(@RequestParam("movieName") String movieName) {
        // 记录查询开始时间
        long startTime = System.currentTimeMillis();

        // 调用 Service 层方法获取电影版本信息
        List<MovieVersion> versions = movieDetailMapper.findVersionsByMovieName(movieName);

        // 记录查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询耗时
        int queryTimeMs = (int) (endTime - startTime);

        // 构造返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("versions", versions); // 返回电影版本信息
        response.put("queryTimeMs", queryTimeMs); // 返回查询时间

        return response;
    }


    @GetMapping("/movies/types")
    public Map<String, Object> getMovieTypes(@RequestParam("movieName") String movieName) {
        // 记录查询开始时间
        long startTime = System.currentTimeMillis();

        // 调用 Service 层方法获取电影版本信息
        List<String> types = movieDetailMapper.findTypesByMovieName(movieName);

        // 记录查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询耗时
        int queryTimeMs = (int) (endTime - startTime);

        // 构造返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("types", types); // 返回电影版本信息
        response.put("queryTimeMs", queryTimeMs); // 返回查询时间

        return response;
    }





}
