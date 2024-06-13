package com.lucassilvs.clientoauthkeycloak.controller;


import com.lucassilvs.clientoauthkeycloak.service.TesteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TesteService testeService;

    @GetMapping("/test-token/{realm}")
    public ResponseEntity<String> security(@PathVariable String realm){
        testeService.getResourceFromProtectedEndpoint(realm);

        return ResponseEntity.ok("Autorizado");
    }
}
