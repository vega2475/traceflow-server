package com.trace.server.authorizationservice.controller;

import com.trace.server.authorizationservice.client.KeycloakAuthorizationClient;
import com.trace.server.authorizationservice.dto.UserLoginRequestDTO;
import com.trace.server.authorizationservice.dto.UserRefreshTokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class AuthorizationController {

    private final KeycloakAuthorizationClient authorizationClient;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    public AuthorizationController(KeycloakAuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO loginRequest) {
        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "password");
        form.put("client_id", clientId);
        form.put("client_secret", clientSecret);
        form.put("username", loginRequest.email());
        form.put("password", loginRequest.password());

        Map<String, Object> response = authorizationClient.getToken(form);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody UserRefreshTokenDTO refreshTokenRequest) {
        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "refresh_token");
        form.put("client_id", clientId);
        form.put("client_secret", clientSecret);
        form.put("refresh_token", refreshTokenRequest.refreshToken());

        Map<String, Object> response = authorizationClient.getRefreshToken(form);

        return ResponseEntity.ok(response);
    }
}
