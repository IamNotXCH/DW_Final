package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.SupplementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplement")
public class SupplementController {

    @Autowired
    private SupplementService supplementService;

    @GetMapping("/actorMovieCounts")
    public QueryResultDTO<List<List<String>>> getActorMovieCounts() {
        return supplementService.getActorMovieCounts();
    }

    @GetMapping("/directorMovieCounts")
    public QueryResultDTO<List<List<String>>> getDirectorMovieCounts() {
        return supplementService.getDirectorMovieCounts();
    }

    @GetMapping("/movieCountsByType")
    public QueryResultDTO<List<List<String>>> getMovieCountsByType() {
        return supplementService.getMovieCountsByType();
    }

    @GetMapping("/moviesWithMultipleVersions")
    public QueryResultDTO<List<List<String>>> getMoviesWithMultipleVersions() {
        return supplementService.getMoviesWithMultipleVersions();
    }
}