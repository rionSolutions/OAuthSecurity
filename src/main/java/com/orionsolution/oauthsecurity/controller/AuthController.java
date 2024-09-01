package com.orionsolution.oauthsecurity.controller;


import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import com.orionsolution.oauthsecurity.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final OauthService oauthService;

    public AuthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    /**
     * @return AuthorizationDTO
     * @Note Application request authorization for validate your credentials
     */
    @PostMapping("/requestAuthorization")
    public ResponseEntity<AuthorizationDTO> requestAuthorization(@RequestBody RequireSessionDTO requireSessionDTO) {
        return ResponseEntity.ok(oauthService.requestAuthorization(requireSessionDTO));
    }

    /**
     * @return AuthorizationDTO
     * @Note Application request access session for validate if the credentials received is secure and turn the session active
     */
    @PostMapping("/requestAccessSession")
    public ResponseEntity<AuthorizationDTO> registerAccessSession(@RequestBody RequireSessionDTO requireSessionDTO) {
        return ResponseEntity.ok(oauthService.registerApplicationSession(requireSessionDTO));
    }

    /**
     * @return AuthorizationDTO
     * @Note Application request refresh token access for validate if the credentials received is secure and turn the session active
     */
    @PostMapping("/refreshTokenAccess")
    public ResponseEntity<AuthorizationDTO> refreshTokenAccess(@RequestBody RequireSessionDTO requireSessionDTO) {
        return ResponseEntity.ok(oauthService.refreshTokenAccess(requireSessionDTO));
    }


}
