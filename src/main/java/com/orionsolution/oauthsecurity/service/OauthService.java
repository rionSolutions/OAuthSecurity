package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import org.springframework.security.oauth2.jwt.Jwt;
public interface OauthService {
  Jwt registerApplicationSession(RequireSessionDTO sessionDTO);

}
