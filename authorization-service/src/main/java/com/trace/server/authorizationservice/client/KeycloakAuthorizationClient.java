package com.trace.server.authorizationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(name = "keycloak", url = "${keycloak.server-url}/realms/${keycloak.realm}/protocol/openid-connect")
public interface KeycloakAuthorizationClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getToken(@RequestBody Map<String, ?> form);

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getRefreshToken(@RequestBody Map<String, ?> form);
}
