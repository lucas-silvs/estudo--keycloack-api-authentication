package com.lucassilvs.customIdentityProvider.authenticator;

import com.lucassilvs.customIdentityProvider.factory.SecretQuestionCredentialProviderFactory;
import com.lucassilvs.customIdentityProvider.factory.provider.SecretQuestionCredentialProvider;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.CredentialValidator;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.*;

import javax.ws.rs.core.Response;
import java.net.URI;

import static org.keycloak.services.util.CookieHelper.addCookie;

public class SecretQuestionAuthenticator implements CredentialValidator<SecretQuestionCredentialProvider> {

    //Authenticator
    public SecretQuestionCredentialProvider getCredentialProvider(KeycloakSession session) {
        return (SecretQuestionCredentialProvider) session.getProvider(CredentialProvider.class, SecretQuestionCredentialProviderFactory.PROVIDER_ID);
    }


    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return getCredentialProvider(session).isConfiguredFor(realm, user, getType(session));
    }

    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction("SECRET_QUESTION_CONFIG");
    }

    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
        Response challenge = context.form()
                .createForm("secret-question.ftl");
        context.challenge(challenge);
    }

    protected boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("SECRET_QUESTION_ANSWERED");
        boolean result = cookie != null;
        if (result) {
            System.out.println("Bypassing secret question because cookie is set");
        }
        return result;
    }

    public void action(AuthenticationFlowContext context) {
        boolean validated = validateAnswer(context);
        if (!validated) {
            Response challenge = context.form()
                    .setError("badSecret")
                    .createForm("secret-question.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }
        setCookie(context);
        context.success();
    }

    protected boolean validateAnswer(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String secret = formData.getFirst("secret_answer");
        String credentialId = formData.getFirst("credentialId");
        if (credentialId == null || credentialId.isEmpty()) {
            credentialId = getCredentialProvider(context.getSession())
                    .getDefaultCredential(context.getSession(), context.getRealm(), context.getUser()).getId();
        }

        UserCredentialModel input = new UserCredentialModel(credentialId, getType(context.getSession()), secret);
        return getCredentialProvider(context.getSession()).isValid(context.getRealm(), context.getUser(), input);
    }

    protected void setCookie(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        if (config != null) {
            maxCookieAge = Integer.parseInt(config.getConfig().get("cookie.max.age"));

        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
        addCookie(context, "SECRET_QUESTION_ANSWERED", "true",
                uri.getRawPath(),
                null, maxCookieAge,
                null,

                false, true);
    }


}
