package com.lucassilvs.customIdentityProvider.factory.provider;

import com.lucassilvs.customIdentityProvider.authenticator.factory.SecretQuestionAuthenticatorFactory;
import com.lucassilvs.customIdentityProvider.factory.SecretQuestionCredentialProviderFactory;
import com.lucassilvs.customIdentityProvider.models.SecretQuestionCredentialModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.common.util.Time;
import org.keycloak.credential.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;


public class SecretQuestionCredentialProvider implements CredentialProvider<SecretQuestionCredentialModel>, CredentialInputValidator {

    private static final Logger logger = LogManager.getLogger(SecretQuestionCredentialProvider.class);
    protected KeycloakSession session;

    @Override
    public String getType() {
        return SecretQuestionCredentialModel.TYPE;
    }

    @Override
    public SecretQuestionCredentialModel getCredentialFromModel(CredentialModel model) {
        return SecretQuestionCredentialModel.createFromCredentialModel(model);
    }

    @Override
    public CredentialTypeMetadata getCredentialTypeMetadata(CredentialTypeMetadataContext metadataContext) {
        return CredentialTypeMetadata.builder()
                .type(getType())
                .category(CredentialTypeMetadata.Category.TWO_FACTOR)
                .displayName(SecretQuestionCredentialProviderFactory.PROVIDER_ID)
                .helpText("secret-question-text")
                .createAction(SecretQuestionAuthenticatorFactory.PROVIDER_ID)
                .removeable(false)
                .build(session);
    }


    @Override
    public CredentialModel createCredential(RealmModel realm, UserModel user, SecretQuestionCredentialModel credentialModel) {
        if (credentialModel.getCreatedDate() == null) {
            credentialModel.setCreatedDate(Time.currentTimeMillis());
        }
        return getCredentialStore().createCredential(realm, user, credentialModel);
    }

    @Override
    public boolean deleteCredential(RealmModel realm, UserModel user, String credentialId) {
        return getCredentialStore().removeStoredCredential(realm, user, credentialId);
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return getType().equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) return false;
        return getCredentialStore().getStoredCredentialsByTypeStream(realm, user, credentialType).findAny().isPresent();
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!(input instanceof UserCredentialModel)) {
            logger.debug("Expected instance of UserCredentialModel for CredentialInput");
            return false;
        }
        if (!input.getType().equals(getType())) {
            return false;
        }
        String challengeResponse = input.getChallengeResponse();
        if (challengeResponse == null) {
            return false;
        }
        CredentialModel credentialModel = getCredentialStore().getStoredCredentialById(realm, user, input.getCredentialId());
        SecretQuestionCredentialModel sqcm = getCredentialFromModel(credentialModel);
        return sqcm.getSecretQuestionSecretData().getAnswer().equals(challengeResponse);
    }

    public SecretQuestionCredentialProvider(KeycloakSession session) {
        this.session = session;
    }

    private UserCredentialStore getCredentialStore() {
        return session.userCredentialManager();
    }
}
