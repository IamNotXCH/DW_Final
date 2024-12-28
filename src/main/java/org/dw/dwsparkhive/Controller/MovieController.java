package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/versionCount")
    public QueryResultDTO<Long> getMovieVersionCount(@RequestParam String movieName) {
        return movieService.getMovieVersionCount(movieName);
    }

    @GetMapping("/countByType")
    public QueryResultDTO<Long> getMovieCountByType(@RequestParam String typeName) {
        return movieService.getMovieCountByType(typeName);
    }

    @GetMapping("/harryPotter")
    public QueryResultDTO<List<Object>> getHarryPotterMovies() {
        return movieService.getHarryPotterMovies();
    }
}