package com.lucassilvs.customidentityprovider.provider.factory;

import com.lucassilvs.customidentityprovider.provider.provider.CustomUserStorageProvider;
import org.keycloak.Config;
import org.keycloak.component.ComponentFactory;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import static com.lucassilvs.customidentityprovider.provider.CustomUserStorageProviderConstant.*;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {
    private static final Logger log = LoggerFactory.getLogger(CustomUserStorageProviderFactory.class);
    protected final List<ProviderConfigProperty> configMetadata;
    private HttpClient httpClient;


    public CustomUserStorageProviderFactory() {
        httpClient = HttpClient.newHttpClient();
        log.info("Iniciando CustomUserStorageProviderFactory");
//      Adicionando Campos que serão solicitados para conexão com o Keycloak
        
        // Create config metadata
        configMetadata = ProviderConfigurationBuilder.create()
//         URL APP Authenticator
          .property()
            .name(URL_AUTHENTICATOR)
            .label("URL")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("URL base do serviço de autenticação")
            .add()
          .build();
        log.info("Criado CustomUserStorageProviderFactory ");

    }
    @Override
    public CustomUserStorageProvider create(KeycloakSession ksession, ComponentModel model) {
        log.info("Criando CustomUserStorageProvider");
        return new CustomUserStorageProvider(ksession,model);
    }

    @Override
    public void init(Config.Scope scope) {
        // não faz nada meu nobrer
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        // não faz nada meu nobrer
    }

    @Override
    public void close() {
    // não faz nada meu nobrer
    }

    @Override
    public String getId() {
        log.info("[I69] getId()");
        return "custom-user-provider-quarkus-2.0.0";
    }

    @Override
    public String getHelpText() {
        return "";
    }

    // Configuration support methods
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        log.info("validateConfiguration() - Testando conexão..." );
        try {
            String urlString = config.get(URL_AUTHENTICATOR)+"/q/health/live";
            log.info("URL HEALTH: " + urlString);
            HttpRequest requestHealth = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(requestHealth, HttpResponse.BodyHandlers.ofString());


            if(response.statusCode() != 200){
                log.warn("Ocorreu erro de validação de saúde no serviço: " + response.statusCode());
                throw new RuntimeException("Ocorreu erro de validação de saúde no serviço: " + response.statusCode());
            }
            log.info("validateConfiguration() -  Connection OK !" );

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
        log.info("onUpdate()" );
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        log.info("onCreate()" );
    }
}
