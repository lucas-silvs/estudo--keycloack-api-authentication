package com.lucassilvs.clientoauthkeycloak.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class tokenService {

    private final OidcServerProperties oidcServerProperties;

    private CaffeineCache caffeineCache;

    public tokenService(OidcServerProperties oidcServerProperties) {
        this.oidcServerProperties = oidcServerProperties;
    }

    @Cacheable("token")
    public String getToken(){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", oidcServerProperties.getGrantType());
        body.add("client_id", oidcServerProperties.getClientId());
        body.add("client_secret", oidcServerProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(oidcServerProperties.getTokenUri(), HttpMethod.POST, requestEntity, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode tokenResponse = responseEntity.getBody();
            assert tokenResponse != null;
            String accessToken = tokenResponse.get("access_token").asText();
            long expiresIn = tokenResponse.get("expires_in").asLong(); // Tempo de expiração em segundos
            long cacheExpirationTime = (long) (expiresIn * 0.8);


            // Calcula o próximo horário de troca de token
            LocalDateTime nextTokenExchangeTime = LocalDateTime.now().plusSeconds(cacheExpirationTime);

            // Formata o próximo horário de troca de token para exibição
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNextTokenExchangeTime = nextTokenExchangeTime.format(formatter);

            // Loga o próximo horário de troca de token
            System.out.println("Próximo horário de troca de token: " + formattedNextTokenExchangeTime);

            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("token");

            caffeineCacheManager.setCacheSpecification("expireAfterWrite=" + cacheExpirationTime + "s");

            return accessToken;
        } else {
            throw new RuntimeException("Erro ao obter token");
        }
    }
}
