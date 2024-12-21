package com.example.java.Mapper;

import com.example.java.Entity.HarryPotterInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MovieBackRootMapper {

    @Select("SELECT " +
            "COUNT(DISTINCT m.movie_id) AS harry_potter_movie_count, " +
            "COUNT(DISTINCT mv.version_id) AS format_count, " +
            "COUNT(DISTINCT mw.ASIN) AS web_page_count, " +
            "(COUNT(DISTINCT mw.ASIN) - COUNT(DISTINCT mv.version_id)) AS merged_web_page_count " +
            "FROM movie m " +
            "LEFT JOIN movie_version mv ON m.movie_id = mv.movie_id " +
            "LEFT JOIN movie_version_web mw ON mv.version_id = mw.version_id " +
            "WHERE m.name LIKE '%Harry Potter%'")
    HarryPotterInfo findHarryPotterInfo();


    // 新增方法：查询 drop_product 表的总数
    @Select("SELECT COUNT(*) FROM drop_product")
    int findDropProductCount();

}
