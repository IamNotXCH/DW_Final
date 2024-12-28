package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping("/movieCount")
    public QueryResultDTO<List<Object>> getActorMovieCount(@RequestParam String actorName) {
        return actorService.getActorMovieCount(actorName);
    }

    @GetMapping("/frequentActorPairs")
    public QueryResultDTO<List<List<String>>> getFrequentActorPairs() {
        return actorService.getFrequentActorPairs();
    }

    @GetMapping("/popularActorPairs")
    public QueryResultDTO<List<List<String>>> getPopularActorPairsByType(@RequestParam String typeName) {
        return actorService.getPopularActorPairsByType(typeName);
    }
}