package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.SupplementaryService;
import org.dw.datawarehousehivebackend.Service.SupplementaryService.TypeMovieCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/hive/supplementary")
public class SupplementaryController {

    @Autowired
    private SupplementaryService supplementaryService;

    // 1. 按演员统计其参演的电影
    @GetMapping("/actors/{actorName}/movies")
    public QueryResultDTO<List<String>> getMoviesByActor(
            @PathVariable String actorName) {
        return supplementaryService.getMoviesByActor(actorName);
    }

    // 2. 按导演统计其导演的电影
    @GetMapping("/directors/{directorName}/movies")
    public QueryResultDTO<List<String>> getMoviesByDirector(
            @PathVariable String directorName) {
        return supplementaryService.getMoviesByDirector(directorName);
    }

    // 3. 按类型统计电影数量
    @GetMapping("/types/movie-count")
    public QueryResultDTO<List<TypeMovieCountDTO>> getMovieCountByType() {
        return supplementaryService.getMovieCountByType();
    }

    // 4. 查询所有有多个版本的电影
    @GetMapping("/movies/multiple-versions")
    public QueryResultDTO<List<String>> getMoviesWithMultipleVersions() {
        return supplementaryService.getMoviesWithMultipleVersions();
    }
}
