package org.dw.dwsparkhive.Controller;

import org.dw.dwsparkhive.DTO.QueryResultDTO;
import org.dw.dwsparkhive.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/highScoreMovies")
    public QueryResultDTO<List<String>> getHighScoreMovies(@RequestParam double scoreThreshold) {
        return reviewService.getHighScoreMovies(scoreThreshold);
    }

    @GetMapping("/positiveReviewedMovies")
    public QueryResultDTO<List<Object>> getPositiveReviewedMovies() {
        return reviewService.getPositiveReviewedMovies();
    }

    @GetMapping("/collaborations/positive-movies-by-type")
    public QueryResultDTO<List<List<String>>> getTopActorPairsByTypeWithMostPositiveReviews(@RequestParam String typeName) {
        return reviewService.getTopActorPairsByTypeWithMostPositiveReviews(typeName);
    }
}