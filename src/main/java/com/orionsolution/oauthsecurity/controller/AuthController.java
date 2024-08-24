package com.orionsolution.oauthsecurity.controller;


import com.orionsolution.oauthsecurity.model.CredentialsDTO;
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

  @PostMapping("/getAccessToken}")
  public ResponseEntity<?> getTokenAccess(@RequestBody CredentialsDTO credentials) {
    log.info(credentials.toString());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh}")
  public ResponseEntity<?> refreshTokenAccess(@RequestHeader(name = "authorization") String token) {
    log.info(token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/verifyRoleAccess}")
  public ResponseEntity<?> verifyRoleAccess(@RequestHeader(name = "authorization") String token) {
    log.info(token);
    return ResponseEntity.ok().build();
  }

}
