package com.example.java.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info().title("DSM系统最终作业 API")
                        .version(appVersion)
                        .description("DSM系统最终作业")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact().email("aaa.com").name("aaa").url("https://aaa.com")));
    }
}