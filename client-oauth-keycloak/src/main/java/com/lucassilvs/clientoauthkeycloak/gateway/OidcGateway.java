package com.lucassilvs.clientoauthkeycloak.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.lucassilvs.clientoauthkeycloak.service.OidcServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class OidcGateway {

    private final OidcServerProperties oidcServerProperties;

    @Autowired
    public OidcGateway(OidcServerProperties oidcServerProperties) {
        this.oidcServerProperties = oidcServerProperties;
    }


    public ResponseEntity<JsonNode> getTokenOidcProvider() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", oidcServerProperties.getGrantType());
        body.add("client_id", oidcServerProperties.getClientId());
        body.add("client_secret", oidcServerProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(oidcServerProperties.getTokenUri(), HttpMethod.POST, requestEntity, JsonNode.class);
    }

}
