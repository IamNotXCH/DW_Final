package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    @GetMapping("/movieCount")
    public QueryResultDTO<Long> getDirectorMovieCount(@RequestParam String directorName) {
        return directorService.getDirectorMovieCount(directorName);
    }

    @GetMapping("/collaborations")
    public QueryResultDTO<List<List<String>>> getDirectorsWithActors() {
        return directorService.getDirectorsWithActors();
    }
}