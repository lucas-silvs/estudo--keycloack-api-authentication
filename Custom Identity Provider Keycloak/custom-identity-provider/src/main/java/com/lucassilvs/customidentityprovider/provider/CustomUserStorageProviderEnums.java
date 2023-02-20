package com.lucassilvs.customidentityprovider.provider;

public enum CustomUserStorageProviderEnums {
    CONFIG_KEY_JDBC_DRIVER("com.mysql.cj.jdbc.Driver"),
    CONFIG_KEY_JDBC_URL ("jdbc:mysql://127.0.0.1:3303/keycloak_oidp"),
    CONFIG_KEY_DB_USERNAME ("idp-user"),
    CONFIG_KEY_DB_PASSWORD ("idp-password"),
    CONFIG_KEY_VALIDATION_QUERY ("SELECT 1");

    private String valor;

    CustomUserStorageProviderEnums(String valor) {
        this.valor = valor;
    }


    public String getValorCampo(){
        return valor;
    }
}
