package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborations")
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    @GetMapping("/frequentActorPairs")
    public QueryResultDTO<List<List<String>>> getFrequentActorPairs() {
        return collaborationService.getFrequentActorPairs();
    }

    @GetMapping("/popularActorPairs")
    public QueryResultDTO<List<List<String>>> getPopularActorPairsByType(@RequestParam String typeName) {
        return collaborationService.getPopularActorPairsByType(typeName);
    }
}