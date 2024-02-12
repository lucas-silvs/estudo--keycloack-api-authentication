package com.lucassilvs.clientoauthkeycloak.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.lucassilvs.clientoauthkeycloak.service.properties.OidcClientProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class OidcGateway {




    public ResponseEntity<JsonNode> getTokenOidcProvider(OidcClientProperties oidcClientProperties) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", oidcClientProperties.getGrantType());
        body.add("client_id", oidcClientProperties.getClientId());
        body.add("client_secret", oidcClientProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(oidcClientProperties.getTokenUri(), HttpMethod.POST, requestEntity, JsonNode.class);
    }

}
