package com.trace.server.authorizationservice.service.impl;

import com.trace.server.authorizationservice.dto.UserRegistrationRequestDTO;
import com.trace.server.authorizationservice.exception.UserRegistrationException;
import com.trace.server.authorizationservice.service.KeycloakService;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultKeycloakService implements KeycloakService {


    private static final Logger log = LoggerFactory.getLogger(DefaultKeycloakService.class);
    private final Keycloak keycloak;

    private final String realm;

    public DefaultKeycloakService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public void addUser(UserRegistrationRequestDTO userRequest) throws UserRegistrationException {
        CredentialRepresentation credential = createPasswordCredentials(userRequest.password());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.email());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        user.setEmailVerified(false);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("country", Collections.singletonList(userRequest.country()));
        user.setAttributes(attributes);


        UsersResource usersResource = getUsersResource();
        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new UserRegistrationException("User with this username already exists.");
            } else if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new UserRegistrationException("User with this username already exists. " + response.getStatus());
            } else {
                log.info("User created successfully.");

                List<UserRepresentation> userRepresentations = usersResource.searchByEmail(user.getEmail(), true);

                if(!userRepresentations.isEmpty()){
                    UserRepresentation representation = userRepresentations.stream().filter(userRepresentation ->
                            Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                    sendEmailVerification(representation.getId());
                }
            }
        } catch (ClientErrorException e) {
            log.error("Client error occurred: {}", e.getResponse().getStatus());
            throw new UserRegistrationException(e.getMessage(), e.getCause());
        } catch (ServerErrorException e) {
            log.error("Server error occurred: {}", e.getResponse().getStatus());
            throw new UserRegistrationException(e.getMessage(), e.getCause());
        } catch (ProcessingException e) {
            log.error("Processing error occurred: {}", e.getMessage());
            throw new UserRegistrationException(e.getMessage(), e.getCause());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new UserRegistrationException(e.getMessage(), e.getCause());
        }
    }


    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    @Override
    public void sendEmailVerification(String userId){
        UsersResource usersResource = getUsersResource();
        UserResource userResource = usersResource.get(userId);
        if(userResource != null){
            userResource.sendVerifyEmail();
            log.info("Email was send to user with id - {}", userId);
        }
    }
}
