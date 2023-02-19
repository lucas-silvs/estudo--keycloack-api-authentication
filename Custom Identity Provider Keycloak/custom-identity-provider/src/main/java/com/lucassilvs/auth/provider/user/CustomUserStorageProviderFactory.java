package com.lucassilvs.auth.provider.user;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import static com.lucassilvs.auth.provider.user.CustomUserStorageProviderConstants.*;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {
    private static final Logger log = LoggerFactory.getLogger(CustomUserStorageProviderFactory.class);
    protected final List<ProviderConfigProperty> configMetadata;
    
    public CustomUserStorageProviderFactory() {
        log.info("[I24] CustomUserStorageProviderFactory created");
//      Adicionando Campos que serão solicitados para conexão com o Keycloak
        
        // Create config metadata
        configMetadata = ProviderConfigurationBuilder.create()
//         Driver Banco de dados
          .property()
            .name(CONFIG_KEY_JDBC_DRIVER)
            .label("Driver JDBC")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("nome completo da classe do Driver para conexão com banco de dados")
            .add()
//         URL conexão Banco de dados
          .property()
            .name(CONFIG_KEY_JDBC_URL)
            .label("JDBC URL")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("URL de conexão com o banco de dados")
            .add()
//         Nome usuário banco de dados
          .property()
            .name(CONFIG_KEY_DB_USERNAME)
            .label("Nome Usuário")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("nome do usuário para conexão com banco de dados")
            .add()
//         Senha usuário banco de dados
          .property()
            .name(CONFIG_KEY_DB_PASSWORD)
            .label("Senha Usuário")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("Senha do usuário para conexão com banco de dados")
            .secret(true)
            .add()
//          Query para validação de conexão
          .property()
            .name(CONFIG_KEY_VALIDATION_QUERY)
            .label("Query SQL validacao")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText("Query SQL para validação de conexão com banco de dados")
            .defaultValue("select 1")
            .add()
          .build();   
          
    }

    @Override
    public CustomUserStorageProvider create(KeycloakSession ksession, ComponentModel model) {
        log.info("[I63] creating new CustomUserStorageProvider");
        return new CustomUserStorageProvider(ksession,model);
    }

    @Override
    public String getId() {
        log.info("[I69] getId()");
        return "custom-user-provider-teste";
    }

    
    // Configuration support methods
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        
       try (Connection c = DbUtil.getConnection(config)) {
           log.info("[I84] Testing connection..." );
           c.createStatement().execute(config.get(CONFIG_KEY_VALIDATION_QUERY));
           log.info("[I92] Connection OK !" );
       }
       catch(Exception ex) {
           log.warn("[W94] Unable to validate connection: ex={}", ex.getMessage());
           throw new ComponentValidationException("Unable to validate database connection",ex);
       }
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
        log.info("[I94] onUpdate()" );
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        log.info("[I99] onCreate()" );
    }
}