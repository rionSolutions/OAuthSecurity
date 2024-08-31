package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;


public interface OauthService {

    AuthorizationDTO registerApplicationSession(RequireSessionDTO sessionDTO);

    AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO);
}
