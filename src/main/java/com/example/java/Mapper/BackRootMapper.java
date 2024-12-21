package com.example.java.Mapper;

import com.example.java.DTO.MovieStatisticsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BackRootMapper {
    // 查询哈利波特系列电影、版本和网页合并数量
    @Select("SELECT " +
            "COUNT(DISTINCT m.movie_id) AS movieCount, " +
            "COUNT(DISTINCT mv.version_id) AS versionCount, " +
            "COUNT(DISTINCT mvw.ASIN) AS webCount " +
            "FROM movie m " +
            "JOIN movie_version mv ON m.movie_id = mv.movie_id " +
            "LEFT JOIN movie_version_web mvw ON mv.version_id = mvw.version_id " +
            "WHERE m.name LIKE '%Harry Potter%'")
    MovieStatisticsDTO findHarryPotterStatistics();


    // 新增方法：查询 drop_product 表的总数
    @Select("SELECT COUNT(*) FROM drop_product")
    int findDropProductCount();
}
