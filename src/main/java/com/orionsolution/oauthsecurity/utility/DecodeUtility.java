package com.orionsolution.oauthsecurity.utility;

import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import com.orionsolution.oauthsecurity.exception.BusinessException;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class DecodeUtility {

    public static void validatePermissions(RequireSessionDTO sessionDTO, Claims claims, List<PermissionsEntity> permissionsEntityList) {
        final String[] notProcess = {"sub", "exp", "iat", "jti", "iss", "aud"};
        List<String> decoded = new ArrayList<>();
        claims.forEach((key, value) -> {
            if (Arrays.asList(notProcess).contains(key)) {
                return;
            }
            decoded.add(processBas64Twice(String.valueOf(value)));
        });
        permissionsEntityList.stream().filter(permission -> !decoded.contains(sessionDTO.getRequired())).forEach(permission -> {
            throw new BusinessException.HandlerException("Permission not found", HttpStatus.UNAUTHORIZED);
        });
    }

    private static String processBas64Twice(String value) {
        final String decoded = getDecoded(value);
        if (!decoded.contains("=")) {
            return decoded;
        }
        return processBas64Twice(decoded);
    }

    private static String getDecoded(String value) {
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
