package com.trace.server.authorizationservice.controller;

import com.trace.server.authorizationservice.dto.UserRegistrationRequestDTO;
import com.trace.server.authorizationservice.exception.UserRegistrationException;
import com.trace.server.authorizationservice.payload.UserAlreadyExistsErrorPayload;
import com.trace.server.authorizationservice.service.KeycloakService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/create")
public class RegistrationController {
    private final KeycloakService keycloakService;

    public RegistrationController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationRequestDTO userRequestDTO, @Value("${keycloak.server-url}") String clientAddress) {
        keycloakService.addUser(userRequestDTO);
        return ResponseEntity.created(URI.create(clientAddress)).body("User successfully created");
    }

    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<UserAlreadyExistsErrorPayload> handleUserRegistrationException (UserRegistrationException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserAlreadyExistsErrorPayload(exception.getMessage(), exception.getTimestamp()));
    }
}
