package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;


public interface OauthService {

    AuthorizationDTO refreshTokenAccess(RequireSessionDTO requireSessionDTO);

    AuthorizationDTO registerApplicationSession(String systemOrigin);

    AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO);
}
