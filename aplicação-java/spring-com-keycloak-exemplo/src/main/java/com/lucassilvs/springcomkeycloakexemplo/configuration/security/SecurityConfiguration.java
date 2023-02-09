package com.lucassilvs.springcomkeycloakexemplo.configuration.security;

import com.lucassilvs.springcomkeycloakexemplo.configuration.KeycloakJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Profile("jwt")
public class SecurityConfiguration {

    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;


    public SecurityConfiguration(KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter) {
        this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz.requestMatchers("/security/**").authenticated())
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(keycloakJwtAuthenticationConverter);


        return http.build();

    }
}
