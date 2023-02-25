package com.lucassilvs.customidentityprovider.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;

import java.util.*;
import java.util.stream.Stream;


public class CustomUser implements UserModel {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthDate;

    private String id;
    private KeycloakSession session;

    private RealmModel realm;

    private ComponentModel model;

    private Long createdTimeStamp;

    private boolean isEnabled;

    private boolean isEmailVerified;

    private List<GroupModel> listGroups;

    private List<String> requiredActions;

    private List<RoleModel> realmRoleModels;

    private List<RoleModel> roleModels;


    private Map<String, List<String>> listAttributes;


    private String federationLink;

    private String serviceAccountLink;

    private CustomUser(KeycloakSession session, RealmModel realm,
                       ComponentModel storageProviderModel,
                       String username,
                       String email,
                       String firstName,
                       String lastName,
                       Date birthDate) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        this.session = session;
        this.realm = realm;
        this.model = storageProviderModel;
        this.createdTimeStamp = System.currentTimeMillis();
        this.isEmailVerified = false;
        this.listGroups = new ArrayList<>();
    }

    @Override
    public String getId() {
        return "f:" + model.getId() + ":" + realm.getId();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String s) {
        this.username = s;
    }

    @Override
    public Long getCreatedTimestamp() {
        return createdTimeStamp;
    }

    @Override
    public void setCreatedTimestamp(Long aLong) {
        this.createdTimeStamp = aLong;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean b) {
        this.isEnabled = b;
    }

    @Override
    public void setSingleAttribute(String s, String s1) {
        List<String> singleAtribute = listAttributes.get(s);
        singleAtribute.add(s1);
        setAttribute(s, singleAtribute);
    }

    @Override
    public void setAttribute(String s, List<String> list) {
        listAttributes.put(s, list);
    }

    @Override
    public void removeAttribute(String s) {
        listAttributes.remove(s);
    }

    @Override
    public String getFirstAttribute(String s) {

        return listAttributes.get(s).stream().findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAttributeStream(String s) {
        return this.listAttributes.get(s).stream();
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String s) {
        this.firstName = s;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String s) {
        this.lastName = s;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String s) {
        this.email = s;
    }

    @Override
    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    @Override
    public void setEmailVerified(boolean b) {
        this.isEmailVerified = b;

    }

    @Override
    public Stream<GroupModel> getGroupsStream() {
        return listGroups.stream();
    }

    @Override
    public void joinGroup(GroupModel groupModel) {
        listGroups.add(groupModel);
    }

    @Override
    public void leaveGroup(GroupModel groupModel) {
        listGroups.remove(groupModel);
    }

    @Override
    public boolean isMemberOf(GroupModel groupModel) {
        return listGroups.contains(groupModel);
    }

    @Override
    public String getFederationLink() {
        return federationLink;
    }

    @Override
    public void setFederationLink(String s) {
        this.federationLink = s;
    }

    @Override
    public String getServiceAccountClientLink() {
        return serviceAccountLink;
    }

    @Override
    public void setServiceAccountClientLink(String s) {
        this.serviceAccountLink = s;
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return null;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return listAttributes;
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return this.requiredActions.stream();
    }

    @Override
    public void addRequiredAction(String s) {
        this.requiredActions.add(s);
    }

    @Override
    public void removeRequiredAction(String s) {
        this.requiredActions.remove(s);
    }

    @Override
    public Stream<RoleModel> getRealmRoleMappingsStream() {
        return realmRoleModels.stream();
    }

    @Override
    public Stream<RoleModel> getClientRoleMappingsStream(ClientModel clientModel) {
        return clientModel.getRolesStream();
    }

    @Override
    public boolean hasRole(RoleModel roleModel) {
        return roleModels.contains(roleModel);
    }

    @Override
    public void grantRole(RoleModel roleModel) {
        roleModels.add(roleModel);
    }

    @Override
    public Stream<RoleModel> getRoleMappingsStream() {

        return roleModels.stream();
    }

    @Override
    public void deleteRoleMapping(RoleModel roleModel) {
        roleModels.remove(roleModel);
    }

    public static class Builder {
        private final KeycloakSession session;
        private final RealmModel realm;
        private final ComponentModel storageProviderModel;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Date birthDate;

        public Builder(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, String username) {
            this.session = session;
            this.realm = realm;
            this.storageProviderModel = storageProviderModel;
            this.username = username;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public CustomUser build() {
            return new CustomUser(
                    session,
                    realm,
                    storageProviderModel,
                    username,
                    email,
                    firstName,
                    lastName,
                    birthDate);
        }
    }
}