package com.orionsolution.oauthsecurity.utility;

import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import com.orionsolution.oauthsecurity.entity.SessionEntity;
import com.orionsolution.oauthsecurity.exception.BusinessException;
import com.orionsolution.oauthsecurity.model.PermissionAppDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public final class JwtUtility extends DecodeUtility {

    /**
     * @param token JWT
     * @return subject from token
     */
    public static String getSubjectFromToken(String token) {
        String tokenWithoutSign = token.split("\\.")[1];
        String jsonString = DecodeUtility.getDecoded(tokenWithoutSign);
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getString("sub");
    }

    /**
     * @param session               SessionEntity
     * @param permissionsEntityList List<PermissionsEntity>
     * @return SecretKey
     */
    public static @NotNull SecretKey recoverSecretKey(SessionEntity session,
                                                      List<PermissionsEntity> permissionsEntityList) {
        StringBuilder aggregateKey = new StringBuilder();
        if (session != null && session.getApplicationRole() != null && session.getApplicationRole().getApplicationEntity() != null) {
            aggregateKey.append(session.getApplicationRole().getApplicationEntity().getApplicationId());
        }
        if (permissionsEntityList != null) {
            permissionsEntityList.forEach(permissionEntity -> aggregateKey.append('.').append(permissionEntity.getPermissionName()));
        }
        if (aggregateKey.isEmpty()) {
            throw new BusinessException.HandlerException("Cannot process, incomplete information available", HttpStatus.UNAUTHORIZED);
        }
        log.info("Recovered secret key: {}", aggregateKey);
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(aggregateKey.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public static String getJWT(RequireSessionDTO sessionDTO, List<PermissionAppDTO> permissionAppDTOList) {
        Map<String, Object> claims = new HashMap<>();
        StringBuilder aggregateKey = new StringBuilder().append(sessionDTO.getClient_secret());
        List<String> roles = new ArrayList<>();
        permissionAppDTOList.forEach(permissionAppDTO -> {
            roles.add(permissionAppDTO.getPermissionName());
            aggregateKey.append('.').append(permissionAppDTO.getPermissionName());
        });

        claims.put("roles", roles);
        claims.put("scope", "read write");
        claims.put("iss", "http://localhost:8081/oauth/api/v1/requestAccessSession");

        //signature is  HMACSHA512(aggregateKey) composed by applicationHeader and sessionDTO.getCredential() more permissionAppDTOList
        log.info("secret key: {}", aggregateKey);
        final SecretKey secretKey =
                Keys.hmacShaKeyFor(Base64.getEncoder().encode(aggregateKey.toString().getBytes(StandardCharsets.UTF_8)));

        return getJWT(sessionDTO.getSession_id(), claims, secretKey);
    }

    public static String getJWT(String session_id, Map<String, Object> claims, SecretKey secretKey) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "SHA512");
        headers.put("typ", "JWT");

        return Jwts.builder()
                .setClaims(claims)
                .setHeader(headers)
                .setAudience("https://orion-softwares.com.br")
                .setSubject(session_id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) //10 minutes
                .signWith(secretKey)
                .compact();
    }
}
