package com.example.java.Mapper;

import com.example.java.Entity.CategoryTopMovies;
import com.example.java.Entity.MovieYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//通过
@Mapper
public interface MovieCombineMapper {

    // 查找xx年以来xx指导的所有xx风格的电影
    @Select("SELECT m.name, m.grade, d.year " +
            "FROM movie m " +
            "JOIN movie_director md ON m.movie_id = md.movie_id " +
            "JOIN director dir ON md.director_id = dir.director_id " +
            "JOIN movie_type mt ON m.movie_id = mt.movie_id " +
            "JOIN type t ON mt.type_id = t.type_id " +
            "JOIN date d ON m.date_id = d.date_id " +
            "WHERE dir.director_name = #{directorName} " +
            "AND t.type_name = #{typeName} " +
            "AND d.year >= #{year}")
    List<MovieYear> findMoviesByDirectorAndType(@Param("directorName") String directorName,
                                                @Param("typeName") String typeName,
                                                @Param("year") int year);

    @Select(value = """
            SELECT
                m.name,t.type_name
            FROM
                movie m
            JOIN
                movie_type mt ON m.movie_id = mt.movie_id
            JOIN
                type t ON mt.type_id = t.type_id
            JOIN
                date d ON m.date_id=d.date_id
            WHERE
                d.year = #{year}  -- 请将这里的年份替换为实际的年份
                AND t.type_id = (
                    SELECT
                        type_id
                    FROM
                        (
                            SELECT
                                mt.type_id,
                                COUNT(*) AS type_count
                            FROM
                                movie_type mt
                            JOIN
                                movie m ON mt.movie_id = m.movie_id
                            JOIN 
                                date d ON m.date_id=d.date_id
                            WHERE
                                d.year = #{year}  -- 请将这里的年份替换为实际的年份
                            GROUP BY
                                mt.type_id
                            ORDER BY
                                type_count DESC
                            LIMIT 1
                        ) AS most_common_style
                )
            ORDER BY
                m.grade DESC
            LIMIT 20;""")
    List<CategoryTopMovies> getTopMovieOnMostFrequentStyle(@Param("year") int year);




    // 查找xx年中，评分大于xx的电影 通过
    @Select("SELECT m.name, m.grade, d.year " +
            "FROM movie m " +
            "JOIN date d ON m.date_id = d.date_id " +
            "WHERE d.year = #{year} " +
            "AND m.grade > #{minGrade}")
    List<MovieYear> findMoviesByYearAndGrade(@Param("year") int year, @Param("minGrade") double minGrade);


}
