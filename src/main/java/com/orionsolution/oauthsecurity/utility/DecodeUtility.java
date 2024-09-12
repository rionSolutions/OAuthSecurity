package com.orionsolution.oauthsecurity.utility;

import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import com.orionsolution.oauthsecurity.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DecodeUtility {

    public static void validatePermissions(Claims claims, List<PermissionsEntity> permissionsEntityList, String systemOrigin) {
        List<String> roles = claims.get("roles", List.class);
        permissionsEntityList.stream().filter(permission -> !roles.contains(systemOrigin)).forEach(permission -> {
            throw new BusinessException.HandlerException("Permission not found", HttpStatus.UNAUTHORIZED);
        });
    }

    public static String getDecoded(String value) {
        return new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)));
    }

    public static Claims getClaims(SecretKey secretKey, String authorizationHeader) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getEncoded()))
                .build()
                .parseClaimsJws(authorizationHeader)
                .getBody();
    }
}
