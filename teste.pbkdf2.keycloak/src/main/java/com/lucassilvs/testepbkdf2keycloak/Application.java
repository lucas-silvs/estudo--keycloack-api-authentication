package com.lucassilvs.testepbkdf2keycloak;

import com.lucassilvs.testepbkdf2keycloak.utils.SenhaUtils;

//@SpringBootApplication
public class Application {

    public static void main(String[] args) {

//        {"value":"h691R2XfWRRg2g6yC8esQfgiX6ncVUr7Ol7mcxRCgm7g82eRgYMmZ6KWZR3gDyJ39EjfyHf/HOLOUlspfIUKlA==","salt":"zf9stKWEA+WourYsl9Oukw==","additionalParameters":{}}
        String senhaInformada = "123456";
        String hash = "h691R2XfWRRg2g6yC8esQfgiX6ncVUr7Ol7mcxRCgm7g82eRgYMmZ6KWZR3gDyJ39EjfyHf/HOLOUlspfIUKlA==";
        String salt = "zf9stKWEA+WourYsl9Oukw==";
        int iterations =  27500;



        SenhaUtils senhaUtils = new SenhaUtils();

        senhaUtils.validarSenha(senhaInformada,hash,salt,iterations);

    }

}
