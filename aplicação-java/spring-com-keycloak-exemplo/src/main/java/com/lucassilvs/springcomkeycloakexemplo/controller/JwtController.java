package com.lucassilvs.springcomkeycloakexemplo.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Profile("jwt")
@RequestMapping("/security")
@RestController
public class JwtController {

    @GetMapping
    public ResponseEntity<String> security(){
        return ResponseEntity.ok("Autorizado");
    }

    @GetMapping(value = "/has-role")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<String> isUser() {
        return ResponseEntity.ok("User");
    }

    /**
     * endpoint onde o usuario tem que ter a role admin
     */
    @GetMapping(value = "/is-admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> isAdmin() {
        return ResponseEntity.ok("Admin");
    }

    @GetMapping(value = "/is-teste")
    @PreAuthorize("hasAnyAuthority('ROLE_TESTE','ROLE_ADMIN')")
    public ResponseEntity<String> isTeste() {
        return ResponseEntity.ok("Teste");
    }



}
