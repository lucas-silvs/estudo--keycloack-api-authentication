package com.lucassilvs.customidentityprovider.provider.model;

public class UsuarioCredencialRequest {

    private final String cpf;
    private final String senha;

    public UsuarioCredencialRequest(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public String toString() {
        return "{ \"cpf\": " + cpf +
                ", \"senha\": \"" + senha + "\"" +
                "}";
    }
}
