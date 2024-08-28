package com.orionsolution.oauthsecurity.controller;


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

    @PostMapping("/requestAuthorization")
    public ResponseEntity<?> requestAuthorization(@RequestBody RequireSessionDTO requireSessionDTO) {
        return null;
    }

    @PostMapping("/registerAccessSession")
    public ResponseEntity<?> registerAccessSession(@RequestBody RequireSessionDTO requireSessionDTO) {
        return ResponseEntity.ok(oauthService.registerApplicationSession(requireSessionDTO));
    }


    @PostMapping("/refreshTokenAccess")
    public ResponseEntity<?> refreshTokenAccess(@RequestHeader(name = "authorization") String token) {
        log.info(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verifyRoleAccess")
    public ResponseEntity<?> verifyRoleAccess(@RequestHeader(name = "authorization") String token) {
        log.info(token);
        return ResponseEntity.ok().build();
    }

}
