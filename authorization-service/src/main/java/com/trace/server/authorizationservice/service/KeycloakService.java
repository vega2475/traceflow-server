package com.trace.server.authorizationservice.service;

import com.trace.server.authorizationservice.dto.UserRegistrationRequestDTO;

public interface KeycloakService {
    void addUser(UserRegistrationRequestDTO userRequest);
    void sendEmailVerification(String userId);
}
