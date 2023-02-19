package com.lucassilvs.auth.provider.user;

public final class CustomUserStorageProviderConstants {
    public static final String CONFIG_KEY_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String CONFIG_KEY_JDBC_URL = "jdbc:mysql://127.0.0.1:3303/keycloak_oidp";
    public static final String CONFIG_KEY_DB_USERNAME = "idp-user";
    public static final String CONFIG_KEY_DB_PASSWORD = "idp-password";
    public static final String CONFIG_KEY_VALIDATION_QUERY = "SELECT 1";
}
