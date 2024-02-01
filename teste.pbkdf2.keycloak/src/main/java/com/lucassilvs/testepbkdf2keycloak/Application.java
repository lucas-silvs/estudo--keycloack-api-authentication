package com.lucassilvs.testepbkdf2keycloak;

import com.lucassilvs.testepbkdf2keycloak.utils.SenhaUtils;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        String senhaInformada = "123456";
        String hash = "dGfR2u0FKQq0VXIX6O0HxuxcNXPEWLZmVNsBGrxcpNe3yQVNlo9m1N/YRIx3KldrXjUMqDmDsZ1Y91lTsY868A==";
        String salt = "6L3fSPYeog2ftYnUpE0cQw==";
        int iterations =  27500;



        SenhaUtils senhaUtils = new SenhaUtils();

        senhaUtils.validarSenha(senhaInformada,hash,salt,iterations);

    }

}
