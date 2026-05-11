package com.patterns.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PatternsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatternsDemoApplication.class, args);
    }

    /**
     * Разрешаем CORS для dev-сервера Vite (localhost:5173).
     * В проде фронт обычно проксируется через тот же origin.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                        .allowedMethods("GET", "POST");
            }
        };
    }
}
