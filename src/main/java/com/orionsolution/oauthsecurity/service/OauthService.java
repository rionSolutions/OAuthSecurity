package com.orionsolution.oauthsecurity.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public interface OauthService {

  Jwt generateJWT(String credentials);

  boolean validateCredentials(String credentials);

}
