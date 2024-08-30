package com.trace.server.authorizationservice.controller;


import com.trace.server.authorizationservice.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/{userId}/send-email-verification")
public class SendEmailController {
    private final KeycloakService keycloakService;

    public SendEmailController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PutMapping
    public void sendVerificationEmail(@PathVariable(name = "userId") String userId){
        keycloakService.sendEmailVerification(userId);
    }
}
