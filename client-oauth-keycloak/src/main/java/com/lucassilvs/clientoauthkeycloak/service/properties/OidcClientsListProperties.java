package com.lucassilvs.clientoauthkeycloak.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "oidc")
@Component
public class OidcClientsListProperties {

    private Map<String, OidcClientProperties> clients;


    public Map<String, OidcClientProperties> getClients() {
        return clients;
    }

    public void setClients(Map<String, OidcClientProperties> clients) {
        this.clients = clients;
    }

    public OidcClientProperties getClientByRealm(String realm) {
        return clients.get(realm);
    }



}
