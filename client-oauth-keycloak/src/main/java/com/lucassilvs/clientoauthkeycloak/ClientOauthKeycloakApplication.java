package com.lucassilvs.clientoauthkeycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ClientOauthKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientOauthKeycloakApplication.class, args);
	}

}
