package com.example.aicctvstream.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/capture-file/view/**")
//                .addResourceLocations("file:///home/ec2-user/app/step1/capture-file/")
//                .setCachePeriod(20);
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // origin이 http://localhost:3000에 대해
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://www.cyber-capstone.kro.kr", "http://localhost:5000", "https://www.cyber-capstone.kro.kr")
                // GET, POST, PATCH, DELETE, OPTIONS 메서드 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
