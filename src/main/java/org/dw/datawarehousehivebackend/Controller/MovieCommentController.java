package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.MovieCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hive/moviecomment")
public class MovieCommentController {

    @Autowired
    private MovieCommentService movieCommentService;

    // 获取用户评分3分以上的电影列表
    @GetMapping("/scores/above/{score}")
    public QueryResultDTO<List<String>> getMoviesWithAverageScoreAbove(@PathVariable int score) {
        return movieCommentService.getMoviesWithAverageScoreAbove(score);
    }

    // 获取用户评价中有正面评价的电影列表
    @GetMapping("/comments/positive")
    public QueryResultDTO<List<String>> getMoviesWithPositiveComments() {
        return movieCommentService.getMoviesWithPositiveComments();
    }
}
