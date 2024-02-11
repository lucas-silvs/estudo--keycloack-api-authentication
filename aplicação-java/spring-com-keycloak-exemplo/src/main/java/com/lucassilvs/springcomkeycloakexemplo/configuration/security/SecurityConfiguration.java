package com.lucassilvs.springcomkeycloakexemplo.configuration.security;

import com.google.common.cache.CacheBuilder;
import com.lucassilvs.springcomkeycloakexemplo.configuration.KeycloakJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Profile("jwt")
public class SecurityConfiguration {

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;


    public SecurityConfiguration(KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter) {
        this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz.requestMatchers("/security/**").authenticated())

                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwt -> jwt.decoder(
                                        jwksCacheDecoder())
                                        .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter))
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwksCacheDecoder(){
        var jwkSetCache = new ConcurrentMapCache("jwkSetCache", CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(30))
                .build().asMap(), false);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                .restOperations(new RestTemplateBuilder().build())
                .cache(jwkSetCache)
                .build();

    }
}
