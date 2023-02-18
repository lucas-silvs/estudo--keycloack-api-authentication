package com.lucassilvs.customIdentityProvider.authenticator.factory;

import com.lucassilvs.customIdentityProvider.authenticator.SecretQuestionAuthenticator;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.KeycloakSession;

public class SecretQuestionAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory, UserPr {

    public static final String PROVIDER_ID = "secret-question-authenticator";
    private static final SecretQuestionAuthenticator SINGLETON = new SecretQuestionAuthenticator();

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }
}
