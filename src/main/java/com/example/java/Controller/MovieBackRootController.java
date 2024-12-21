package com.example.java.Controller;

import com.example.java.Entity.HarryPotterInfo;
import com.example.java.Mapper.MovieBackRootMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MovieBackRootController {

    @Autowired
    private MovieBackRootMapper movieBackRootMapper;

    @GetMapping("/harry-potter-info")
    @ResponseBody
    public Map<String, Object> getHarryPotterInfo() {
        // 记录查询开始时间
        long startTime = System.currentTimeMillis();

        // 执行查询
        HarryPotterInfo result = movieBackRootMapper.findHarryPotterInfo();

        // 记录查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询耗时
        long duration = endTime - startTime;

        // 构建响应对象
        Map<String, Object> response = new HashMap<>();
        response.put("data", result);
        response.put("queryTime", duration); // 查询时间（毫秒）

        return response;
    }

    @GetMapping("/drop-product-count")
    @ResponseBody
    public Map<String, Object> getDropProductCount() {
        // 记录查询开始时间
        long startTime = System.currentTimeMillis();

        // 执行查询
        int count = movieBackRootMapper.findDropProductCount();

        // 记录查询结束时间
        long endTime = System.currentTimeMillis();

        // 计算查询耗时
        long duration = endTime - startTime;

        // 构建响应对象
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("queryTime", duration); // 查询时间（毫秒）

        return response;
    }
}

