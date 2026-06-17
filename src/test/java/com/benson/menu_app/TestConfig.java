package com.benson.menu_app;

import com.benson.menu_app.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}

