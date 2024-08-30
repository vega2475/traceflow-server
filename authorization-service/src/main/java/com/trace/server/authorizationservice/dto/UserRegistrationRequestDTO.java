package com.trace.server.authorizationservice.dto;

public record UserRegistrationRequestDTO (String email, String password, String country, String firstName, String lastName) {
}
