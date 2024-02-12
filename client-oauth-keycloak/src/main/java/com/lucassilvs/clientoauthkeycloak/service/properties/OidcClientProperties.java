package com.lucassilvs.clientoauthkeycloak.service.properties;



public class OidcClientProperties {

    private  String clientId;
    private  String clientSecret;
    private  String tokenUri;
    private  String grantType;
    private  String scope;

    public OidcClientProperties(String clientId, String clientSecret, String tokenUri, String grantType, String scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
        this.grantType = grantType;
        this.scope = scope;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getScope() {
        return scope;
    }
}
