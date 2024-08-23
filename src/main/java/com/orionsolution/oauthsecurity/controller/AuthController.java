package com.orionsolution.oauthsecurity.controller;


import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @GetMapping("/get_credentials/{credentials}")
  public ResponseEntity<?> getTokenAcess(@PathVariable("credentials") String credentials) {
    log.info(credentials);
    return ResponseEntity.ok().build();
  }
}
