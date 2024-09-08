package com.orionsolution.oauthsecurity.controller;


import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import com.orionsolution.oauthsecurity.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController handles OAuth-related requests such as authorization, session registration, and token refresh.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final OauthService oauthService;

    /**
     * Constructs an AuthController with the specified OauthService.
     *
     * @param oauthService the service to handle OAuth operations
     */
    public AuthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    /**
     * Handles the request for authorization.
     *
     * @param formParams the form parameters containing the authorization request details
     * @return a ResponseEntity containing the AuthorizationDTO
     * @Note Application request authorization for validate your credentials
     */
    @PostMapping(value = "/requestAuthorization", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthorizationDTO> requestAuthorization(@RequestParam Map<String, String> formParams) {
        RequireSessionDTO requireSessionDTO = new RequireSessionDTO(formParams);
        return ResponseEntity.ok(oauthService.requestAuthorization(requireSessionDTO));
    }

    /**
     * Handles the request to register an access session.
     *
     * @param systemOrigin the origin of the system making the request
     * @return a ResponseEntity containing the AuthorizationDTO
     * @Note Application request access session for validate if the credentials received is secure and turn the session active
     */
    @PostMapping("/requestAccessSession")
    public ResponseEntity<AuthorizationDTO> registerAccessSession(@RequestHeader("systemOrigin") String systemOrigin) {
        return ResponseEntity.ok(oauthService.registerApplicationSession(systemOrigin));
    }

    /**
     * Handles the request to refresh the token access.
     *
     * @param formParams the form parameters containing the refresh token request details
     * @return a ResponseEntity containing the AuthorizationDTO
     * @Note Application request refresh token access for validate if the credentials received is secure and turn the session active
     */
    @PostMapping("/refreshTokenAccess")
    public ResponseEntity<AuthorizationDTO> refreshTokenAccess(@RequestParam Map<String, String> formParams) {
        RequireSessionDTO requireSessionDTO = new RequireSessionDTO(formParams);
        return ResponseEntity.ok(oauthService.refreshTokenAccess(requireSessionDTO));
    }
}
