package com.lucassilvs.clientoauthkeycloak.service.properties;

public class TokenProperties {

    private String AccessToken;
    private String TokenType;
    private long ExpiresIn;
    private String Scope;

    private String RefreshToken;
    private long refreshTokenExpiresIn;

    public String getAccessToken() {
        return AccessToken;
    }

    public String getTokenType() {
        return TokenType;
    }

    public long getExpiresIn() {
        return ExpiresIn;
    }

    public String getScope() {
        return Scope;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public void setTokenType(String tokenType) {
        TokenType = tokenType;
    }

    public void setExpiresIn(long expiresIn) {
        ExpiresIn = expiresIn;
    }

    public void setScope(String scope) {
        Scope = scope;
    }

    public void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }

    public void setRefreshTokenExpiresIn(long refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
