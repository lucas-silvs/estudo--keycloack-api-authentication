package com.lucassilvs.testepbkdf2keycloak.utils;

import org.keycloak.common.crypto.CryptoIntegration;
import org.keycloak.crypto.def.DefaultCryptoProvider;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SenhaUtils {

    //Recebe Hash e Salt PBKDF2 gerada pelo Keycloak e compara com a senha informada usando a Classe PBEKeySpec
    public  String validarSenha(String senhaInformada, String hash, String salt, int iterations) {
        int keyLength = calculaKeyLength(hash);

        byte[] saltBytes = Base64.getDecoder().decode(salt);

        KeySpec spec = new PBEKeySpec(senhaInformada.toCharArray(), saltBytes, iterations , keyLength);

        try {

            byte[] key = getSecretKeyFactory().generateSecret(spec).getEncoded();

            String retorno = org.keycloak.common.util.Base64.encodeBytes(key);
            System.out.println("retorno: " + retorno);
            System.out.println("retorno é igual ao hash: " + retorno.equals(hash));
            return retorno;
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Credential could not be encoded", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private  int calculaKeyLength(String hash) {

        byte[] hashBytes = Base64.getDecoder().decode(hash); //Valida se é um Base64 válido
        return hashBytes.length * 8;
    }

    private  SecretKeyFactory getSecretKeyFactory() {
        try {
            CryptoIntegration.init(this.getClass().getClassLoader());

            DefaultCryptoProvider cryptoProvider = new DefaultCryptoProvider();
            SecretKeyFactory pbkdf2WithHmacSHA256 = cryptoProvider.getSecretKeyFact("PBKDF2WithHmacSHA256");
            return pbkdf2WithHmacSHA256;

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("PBKDF2 algorithm not found", e);
        }
    }
}
