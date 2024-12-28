package org.dw.dwsparkhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.*;

@SpringBootApplication
public class DwSparkHiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(DwSparkHiveApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**") // 允许所有路径
                        .allowedOrigins(
                                "http://localhost:8080",      // 后端自身（如果前端和后端在同一端口）
                                "http://localhost:9528",      // 前端应用程序端口
                                "https://*.guisu.website"     // 其他允许的域名
                        )
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS") // 允许的方法
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true) // 允许发送凭证（如Cookies）
                        .maxAge(3600); // 预检请求的缓存时间（秒）
            }
        };
    }
}
