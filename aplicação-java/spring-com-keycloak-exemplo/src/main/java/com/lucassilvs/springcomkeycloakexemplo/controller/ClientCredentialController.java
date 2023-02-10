package com.lucassilvs.springcomkeycloakexemplo.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Profile("clientCredential")
@RequestMapping("/security")
@RestController
public class ClientCredentialController {

    @GetMapping
    public ResponseEntity<String> security(){
        return ResponseEntity.ok("Autorizado");
    }
}
