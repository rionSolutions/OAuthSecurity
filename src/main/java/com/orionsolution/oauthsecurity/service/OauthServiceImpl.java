package com.orionsolution.oauthsecurity.service;

import org.springframework.security.oauth2.jwt.Jwt;

public class OauthServiceImpl implements OauthService{
    @Override
    public Jwt generateJWT(String credentials) {
        //TODO IF USER EXISTS
        return null;
    }

    @Override
    public boolean validateCredentials(String token) {
        // TODO VALIDATE TOKEN WITH DATABASE
        return false;
    }
}
