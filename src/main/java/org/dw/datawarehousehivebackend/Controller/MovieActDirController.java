package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.MovieActDirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hive/moviedir")
public class MovieActDirController {

    @Autowired
    private MovieActDirService movieActDirService;

    // 获取某导演的电影数量
    @GetMapping("/count/director/{directorName}")
    public QueryResultDTO<Integer> getMovieCountByDirector(@PathVariable String directorName) {
        return movieActDirService.getMovieCountByDirector(directorName);
    }

    // 获取某演员主演的电影数量
    @GetMapping("/count/actor/{actorName}/starred")
    public QueryResultDTO<Integer> getStarredMovieCountByActor(@PathVariable String actorName) {
        return movieActDirService.getStarredMovieCountByActor(actorName);
    }

    // 获取某演员参演的电影数量
    @GetMapping("/count/actor/{actorName}/participated")
    public QueryResultDTO<Integer> getParticipatedMovieCountByActor(@PathVariable String actorName) {
        return movieActDirService.getParticipatedMovieCountByActor(actorName);
    }

    // 获取经常合作的演员列表，合作次数至少为 minCollabCount
    @GetMapping("/collaborations/actors")
    public QueryResultDTO<List<String>> getFrequentCollaboratingActors(@RequestParam(defaultValue = "5") int minCollabCount) {
        return movieActDirService.getFrequentCollaboratingActors(minCollabCount);
    }

    // 获取经常合作的导演和演员列表，合作次数至少为 minCollabCount
    @GetMapping("/collaborations/director-actors")
    public QueryResultDTO<List<String>> getFrequentDirectorActorCollaborations(@RequestParam(defaultValue = "3") int minCollabCount) {
        return movieActDirService.getFrequentDirectorActorCollaborations(minCollabCount);
    }
}
