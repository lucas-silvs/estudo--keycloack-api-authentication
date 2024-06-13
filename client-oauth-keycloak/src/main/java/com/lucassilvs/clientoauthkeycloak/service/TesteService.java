package com.lucassilvs.clientoauthkeycloak.service;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.lucassilvs.clientoauthkeycloak.service.properties.TokenProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class TesteService {


    private final tokenService tokenService;

    @Value("${service.url}")
    private String serviceUrl;

    public TesteService(com.lucassilvs.clientoauthkeycloak.service.tokenService tokenService) {
        this.tokenService = tokenService;
    }


    public String getResourceFromProtectedEndpoint(String realm) {

        String token = tokenService.getToken(realm);

        // Use o RestTemplate com o token de acesso para fazer solicitações autenticadas

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, HttpMethod.GET, requestEntity, String.class);


        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }

    private createCache(TokenProperties tokenProperties){
        LoadingCache<String, TokenProperties> graphs = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, TokenProperties>() {

                    public long expireAfterCreate(String key, TokenProperties graph, long currentTime) {
                        // Use wall clock time, rather than nanotime, if from an external resource
                        long seconds = (long) (graph.getExpiresIn() * 0.8);
                        return TimeUnit.SECONDS.toNanos(seconds);
                    }
                    public long expireAfterUpdate(String key, TokenProperties graph,
                                                  long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                    public long expireAfterRead(String key, TokenProperties graph,
                                                long currentTime, long currentDuration) {
                        return currentDuration;
                    }
                })
                .build(key -> tokenService.getToken(key));
    }
}
