package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OauthServiceImpl implements OauthService {


    @Override
    public Jwt registerApplicationSession(RequireSessionDTO sessionDTO) {


        return getJwt(sessionDTO);
    }

    private static Jwt getJwt(RequireSessionDTO sessionDTO) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        claims.put("sessionId", sessionDTO.getCredential());

        final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a secure key

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(sessionDTO.getRoleClient())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new Jwt(token, Instant.now(), Instant.now().plus(10, ChronoUnit.HOURS), headers, claims);
    }

}
