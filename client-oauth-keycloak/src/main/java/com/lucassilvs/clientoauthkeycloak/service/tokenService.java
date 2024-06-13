package com.lucassilvs.clientoauthkeycloak.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lucassilvs.clientoauthkeycloak.gateway.OidcGateway;
import com.lucassilvs.clientoauthkeycloak.service.properties.OidcClientsListProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class tokenService {

    private final OidcGateway oidcRestClient;

    private final OidcClientsListProperties clientsListProperties;

    @Autowired
    public tokenService(OidcGateway oidcRestClient, OidcClientsListProperties clientsListProperties) {
        this.oidcRestClient = oidcRestClient;
        this.clientsListProperties = clientsListProperties;
    }

    @Cacheable(value = "token", key = "#realm")
    public String getToken(String realm){

        ResponseEntity<JsonNode> responseEntity = oidcRestClient.getTokenOidcProvider(clientsListProperties.getClientByRealm(realm));

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
