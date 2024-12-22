package org.dw.datawarehousehivebackend.Controller;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.dw.datawarehousehivebackend.Service.MovieActDirRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/hive/moviedirrelation")
public class MovieActDirRelationController {

    @Autowired
    private MovieActDirRelationService movieActDirRelationService;

    // 1. 获取经常合作的演员列表
    @GetMapping("/collaborations/actors")
    public QueryResultDTO<List<String>> getFrequentCollaboratingActors(
            @RequestParam(defaultValue = "5") @Min(1) int minCollabCount) {
        return movieActDirRelationService.getFrequentCollaboratingActors(minCollabCount);
    }

    // 2. 获取经常合作的导演和演员列表
    @GetMapping("/collaborations/director-actors")
    public QueryResultDTO<List<String>> getFrequentDirectorActorCollaborations(
            @RequestParam(defaultValue = "3") @Min(1) int minCollabCount) {
        return movieActDirRelationService.getFrequentDirectorActorCollaborations(minCollabCount);
    }

    // 3. 获取指定类型电影中最受关注的演员组合
    @GetMapping("/top-commented-combinations")
    public QueryResultDTO<List<String>> getTopCommentedActorCombinationsByType(
            @RequestParam String typeName,
            @RequestParam(defaultValue = "2") @Min(2) int combinationSize,
            @RequestParam(defaultValue = "10") @Min(1) int topN) {
        return movieActDirRelationService.getTopCommentedActorCombinationsByType(typeName, combinationSize, topN);
    }

    // 其他相关 API 端点可以在这里添加
}
