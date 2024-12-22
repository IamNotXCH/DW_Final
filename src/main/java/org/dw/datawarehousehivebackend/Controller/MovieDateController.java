package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.MovieDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hive/date")
public class MovieDateController {

    @Autowired
    private MovieDateService movieDateService;

    // 获取某年的电影数量
    @GetMapping("/count/year/{year}")
    public QueryResultDTO<Integer> getMovieCountByYear(@PathVariable int year) {
        return movieDateService.getMovieCountByYear(year);
    }

    // 获取某年某月的电影数量
    @GetMapping("/count/year/{year}/month/{month}")
    public QueryResultDTO<Integer> getMovieCountByYearAndMonth(@PathVariable int year, @PathVariable int month) {
        return movieDateService.getMovieCountByYearAndMonth(year, month);
    }

    // 获取某年某季度的电影数量
    @GetMapping("/count/year/{year}/season/{season}")
    public QueryResultDTO<Integer> getMovieCountByYearAndSeason(@PathVariable int year, @PathVariable int season) {
        return movieDateService.getMovieCountByYearAndSeason(year, season);
    }

    // 获取某年某周的电影数量
    @GetMapping("/count/year/{year}/week/{week}")
    public QueryResultDTO<Integer> getMovieCountByYearAndWeek(@PathVariable int year, @PathVariable int week) {
        return movieDateService.getMovieCountByYearAndWeek(year, week);
    }
}
