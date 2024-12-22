package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.MovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hive/movietype")
public class MovieTypeController {

    @Autowired
    private MovieTypeService movieTypeService;

    // 获取某电影的版本数量
    @GetMapping("/count/versions")
    public QueryResultDTO<Integer> getVersionCountByMovieName(@RequestParam String movieName) {
        return movieTypeService.getVersionCountByMovieName(movieName);
    }

    // 获取某类型的电影数量
    @GetMapping("/count/type")
    public QueryResultDTO<Integer> getMovieCountByType(@RequestParam String typeName) {
        return movieTypeService.getMovieCountByType(typeName);
    }

    // 获取所有电影类别及其对应的电影数量
    @GetMapping("/count/types")
    public QueryResultDTO<List<String>> getMovieCountForAllTypes() {
        return movieTypeService.getMovieCountForAllTypes();
    }
}
