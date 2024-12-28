package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    @GetMapping("/collaborations/frequent-director-actor-pairs")
    public QueryResultDTO<List<List<String>>> getFrequentDirectorActorPairs() {
        return collaborationService.getFrequentDirectorActorPairs();
    }

    @GetMapping("/collaborations/top-director-by-type")
    public QueryResultDTO<List<List<Object>>> getTopDirectorByMovieType(@RequestParam String typeName) {
        return collaborationService.getTopDirectorByMovieType(typeName);
    }

    @GetMapping("/collaborations/top-actor-pairs-by-type")
    public QueryResultDTO<List<List<Object>>> getTopActorPairsByType(@RequestParam String typeName) {
        return collaborationService.getTopActorPairsByType(typeName);
    }
}
