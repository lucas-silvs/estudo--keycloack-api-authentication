/**
 *
 */
package com.lucassilvs.customidentityprovider.provider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucassilvs.customidentityprovider.provider.CustomUser;
import com.lucassilvs.customidentityprovider.provider.model.http.request.UsuarioCredencialRequest;
import com.lucassilvs.customidentityprovider.provider.model.http.response.UsuarioResponse;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.lucassilvs.customidentityprovider.provider.CustomUserStorageProviderConstant.URL_AUTHENTICATOR;

public class CustomUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator {

    private static final Logger log = LoggerFactory.getLogger(CustomUserStorageProvider.class);
    private KeycloakSession ksession;
    private ComponentModel model;

    private HttpClient httpClient;

    public CustomUserStorageProvider(KeycloakSession ksession, ComponentModel model) {
        this.ksession = ksession;
        this.model = model;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void close() {
        log.info("[I30] close()");
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.info("[I35] getUserById({})", id);
        StorageId sid = new StorageId(id);
        return getUserByUsername(realm, sid.getExternalId());
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.info("[I48] getUserByEmail({})", email);
        return getUserByUsername(realm, email);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.info("[I41] getUserByUsername({})", username);
        String url = String.format("%s/usuario?identificador=%s", model.get(URL_AUTHENTICATOR), username);

        HttpRequest buscaUsuarioRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            log.info("Buscando usuário aplicação Quarkus teste");
            HttpResponse<String> response = httpClient.send(buscaUsuarioRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            UsuarioResponse userResponse = mapper.readValue(response.body(), UsuarioResponse.class);


            return mapUser(realm, userResponse);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.info("[I57] supportsCredentialType({})", credentialType);
        return PasswordCredentialModel.TYPE.endsWith(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        log.info("[I57] isConfiguredFor(realm={},user={},credentialType={})", realm.getName(), user.getUsername(), credentialType);
        // In our case, password is the only type of credential, so we allways return 'true' if
        // this is the credentialType
        return supportsCredentialType(credentialType);
    }


    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        log.info("[I57] isValid(realm={},user={},credentialInput.type={})", realm.getName(), user.getUsername(), credentialInput.getType());

        if (!this.supportsCredentialType(credentialInput.getType())) {
            return false;
        }
        StorageId sid = new StorageId(user.getId());
        String username = sid.getExternalId();
        UsuarioCredencialRequest request = new UsuarioCredencialRequest(username, credentialInput.getChallengeResponse());
        String json = request.toString();

        HttpRequest validarCredencialRequest = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/usuario/validar-credencial", model.get(URL_AUTHENTICATOR))))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            log.info("Buscando usuário aplicação Quarkus teste");
            HttpResponse<String> response = httpClient.send(validarCredencialRequest, HttpResponse.BodyHandlers.ofString());

            log.info("Status code da validação de credencial: " + response.statusCode());
            if (response.statusCode() == 204) {
                return true;
            } else {
                return false;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private UserModel mapUser(RealmModel realm, UsuarioResponse usuarioResponse) {

        String[] nameSplit = usuarioResponse.getNome().split(" ");
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(usuarioResponse.getDataNascimento());
        String newDateString = fmt.format(usuarioResponse.getDataNascimento());
        Date dateTeste;
        try {
            dateTeste = fmt.parse(newDateString);
        } catch (ParseException e) {
            throw new RuntimeException("ocorreu um erro ao converter data nascimento: " + e);
        }
        CustomUser user = new CustomUser.Builder(ksession, realm, model, usuarioResponse.getCpf())
                .email(usuarioResponse.getEmail())
                .firstName(nameSplit[0])
                .lastName(nameSplit[nameSplit.length - 1])
                .birthDate(dateTeste)
                .build();

        return user;
    }
}
