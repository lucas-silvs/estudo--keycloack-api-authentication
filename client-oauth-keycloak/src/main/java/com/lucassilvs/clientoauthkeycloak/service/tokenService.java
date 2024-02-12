package com.lucassilvs.clientoauthkeycloak.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lucassilvs.clientoauthkeycloak.gateway.OidcGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class tokenService {

    private final OidcGateway oidcClient;

    @Autowired
    public tokenService(OidcGateway oidcClient) {
        this.oidcClient = oidcClient;
    }

    @Cacheable("token")
    public String getToken(){
        ResponseEntity<JsonNode> responseEntity = oidcClient.getTokenOidcProvider();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode tokenResponse = responseEntity.getBody();
            assert tokenResponse != null;
            String accessToken = tokenResponse.get("access_token").asText();


            long expiresIn = tokenResponse.get("expires_in").asLong(); // Tempo de expiração em segundos

            // Calcula o próximo horário de troca de token
            LocalDateTime nextTokenExchangeTime = LocalDateTime.now().plusSeconds((long) (expiresIn * 0.8));

            // Formata o próximo horário de troca de token para exibição
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNextTokenExchangeTime = nextTokenExchangeTime.format(formatter);

            // Loga o próximo horário de troca de token
            System.out.println("Próximo horário de troca de token: " + formattedNextTokenExchangeTime);


            return accessToken;
        } else {
            throw new RuntimeException(String.format("Erro ao obter token -- Status code: %s ", responseEntity.getStatusCode()));
        }
    }



}
