package com.lucassilvs.clientoauthkeycloak.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TesteService {


    private final tokenService tokenService;

    @Value("${service.url}")
    private String serviceUrl;

    public TesteService(com.lucassilvs.clientoauthkeycloak.service.tokenService tokenService) {
        this.tokenService = tokenService;
    }


    public String getResourceFromProtectedEndpoint() {

        String token = tokenService.getToken();

        // Use o RestTemplate com o token de acesso para fazer solicitações autenticadas

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, HttpMethod.GET, requestEntity, String.class);


        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }
}
