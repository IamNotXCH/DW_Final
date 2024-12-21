package com.example.java.Controller;

import com.example.java.Mapper.BackRootMapper;
import com.example.java.DTO.MovieStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BackRootController {

    @Autowired
    private BackRootMapper backRootMapper;

    // 查询哈利波特系列电影、版本和网页合并数量
    @GetMapping("/harryPotterStatistics")
    public Map<String, Object> getHarryPotterStatistics() {
        // 获取查询开始时间
        long startTime = System.currentTimeMillis();

        // 获取统计数据
        MovieStatisticsDTO statisticsDTO = backRootMapper.findHarryPotterStatistics();

        // 获取查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询时长（单位：毫秒）
        long duration = endTime - startTime;

        // 获取当前时间
        String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 创建一个Map返回数据
        Map<String, Object> response = new HashMap<>();
        response.put("statistics", statisticsDTO);
        response.put("queryTime", queryTime);
        response.put("queryDuration", duration + " ms");

        return response;
    }

    // 查询 drop_product 表的总数
    @GetMapping("/dropProductCount")
    public Map<String, Object> getDropProductCount() {
        // 获取查询开始时间
        long startTime = System.currentTimeMillis();

        // 获取 drop_product 表的记录数
        int dropProductCount = backRootMapper.findDropProductCount();

        // 获取查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询时长（单位：毫秒）
        long duration = endTime - startTime;

        // 创建一个Map返回数据
        Map<String, Object> response = new HashMap<>();
        response.put("dropProductCount", dropProductCount);
        response.put("queryDuration", duration + " ms");

        return response;
    }
}
