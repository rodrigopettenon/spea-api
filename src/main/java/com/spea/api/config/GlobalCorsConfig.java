package com.spea.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    // Lista de origens permitidas (front-end)
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:8081",
            "http://localhost:5173",
            "http://localhost:3000",
            "http://127.0.0.1:8081",
            "http://127.0.0.1:5173"
    );

    // Métodos HTTP permitidos
    private static final List<String> ALLOWED_METHODS = Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    );

    // Headers permitidos
    private static final List<String> ALLOWED_HEADERS = Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization",
            "X-Requested-With", "X-Auth-Token", "X-CSRF-Token"
    );

    /**
     * Configuração global do CORS para todos os endpoints
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica a todas as rotas
                .allowedOrigins(ALLOWED_ORIGINS.toArray(new String[0]))
                .allowedMethods(ALLOWED_METHODS.toArray(new String[0]))
                .allowedHeaders(ALLOWED_HEADERS.toArray(new String[0]))
                .allowCredentials(true)
                .maxAge(3600); // 1 hora de cache para pré-flight requests
    }

    /**
     * Configuração alternativa mais granular via CorsConfigurationSource
     * Útil para integração com Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hora em segundos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas as rotas

        return source;
    }
}