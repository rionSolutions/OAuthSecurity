package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import org.springframework.security.oauth2.jwt.Jwt;


public interface OauthService {
  Jwt registerApplicationSession(RequireSessionDTO sessionDTO);
  AuthorizationDTO requestAuthorization (RequireSessionDTO sessionDTO);
}
