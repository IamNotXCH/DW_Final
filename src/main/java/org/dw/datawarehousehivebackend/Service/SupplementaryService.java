package org.dw.datawarehousehivebackend.Service;

import org.dw.datawarehousehivebackend.DTO.QueryResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplementaryService {

    private static final Logger logger = LoggerFactory.getLogger(SupplementaryService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. 按演员统计其参演的电影
    public QueryResultDTO<List<String>> getMoviesByActor(String actorName) {
        String sql = "SELECT m.name AS movieName " +
                "FROM Movie m " +
                "JOIN Movie_Actor ma ON m.movieId = ma.movieId " +
                "JOIN Actor a ON ma.actorId = a.actorId " +
                "WHERE a.actorName = ? " +
                "ORDER BY m.name ASC";

        long startTime = System.currentTimeMillis();
        List<String> movies = jdbcTemplate.query(sql, new Object[]{actorName}, (rs, rowNum) -> rs.getString("movieName"));
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;

        logger.info("Fetched {} movies for actor {}", movies.size(), actorName);
        return new QueryResultDTO<>(movies, queryTime);
    }

    // 2. 按导演统计其导演的电影
    public QueryResultDTO<List<String>> getMoviesByDirector(String directorName) {
        String sql = "SELECT m.name AS movieName " +
                "FROM Movie m " +
                "JOIN Movie_Director md ON m.movieId = md.movieId " +
                "JOIN Director d ON md.directorId = d.directorId " +
                "WHERE d.directorName = ? " +
                "ORDER BY m.name ASC";

        long startTime = System.currentTimeMillis();
        List<String> movies = jdbcTemplate.query(sql, new Object[]{directorName}, (rs, rowNum) -> rs.getString("movieName"));
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;

        logger.info("Fetched {} movies for director {}", movies.size(), directorName);
        return new QueryResultDTO<>(movies, queryTime);
    }

    // 3. 按类型统计电影数量
    public QueryResultDTO<List<TypeMovieCountDTO>> getMovieCountByType() {
        String sql = "SELECT t.typeName, COUNT(m.movieId) AS movieCount " +
                "FROM Movie m " +
                "JOIN Movie_Type mt ON m.movieId = mt.movieId " +
                "JOIN Type t ON mt.typeId = t.typeId " +
                "GROUP BY t.typeName " +
                "ORDER BY movieCount DESC";

        long startTime = System.currentTimeMillis();
        List<TypeMovieCountDTO> typeMovieCounts = jdbcTemplate.query(sql, (rs, rowNum) -> {
            TypeMovieCountDTO dto = new TypeMovieCountDTO();
            dto.setTypeName(rs.getString("typeName"));
            dto.setMovieCount(rs.getInt("movieCount"));
            return dto;
        });
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;

        logger.info("Fetched movie counts for {} types", typeMovieCounts.size());
        return new QueryResultDTO<>(typeMovieCounts, queryTime);
    }

    // 4. 查询所有有多个版本的电影
    public QueryResultDTO<List<String>> getMoviesWithMultipleVersions() {
        String sql = "SELECT m.name AS movieName, COUNT(v.versionId) AS versionCount " +
                "FROM Movie m " +
                "JOIN Movie_Version v ON m.movieId = v.movieId " +
                "GROUP BY m.name " +
                "HAVING COUNT(v.versionId) > 1 " +
                "ORDER BY m.name ASC";

        long startTime = System.currentTimeMillis();
        List<String> movies = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("movieName"));
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;

        logger.info("Fetched {} movies with multiple versions", movies.size());
        return new QueryResultDTO<>(movies, queryTime);
    }

    // DTO for Type and Movie Count
    public static class TypeMovieCountDTO {
        private String typeName;
        private int movieCount;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getMovieCount() {
            return movieCount;
        }

        public void setMovieCount(int movieCount) {
            this.movieCount = movieCount;
        }
    }
}
