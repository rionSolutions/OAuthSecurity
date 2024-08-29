package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import com.orionsolution.oauthsecurity.entity.ApplicationRoleEntity;
import com.orionsolution.oauthsecurity.entity.SessionEntity;
import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.PermissionAppDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import com.orionsolution.oauthsecurity.repository.ApplicationRoleRepository;
import com.orionsolution.oauthsecurity.repository.SessionRepository;
import com.orionsolution.oauthsecurity.utility.ApplicationKeyUtility;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OauthServiceImpl implements OauthService {

    private final ApplicationRoleRepository applicationRoleRepository;
    private final SessionRepository sessionRepository;

    public OauthServiceImpl(ApplicationRoleRepository applicationRoleRepository, SessionRepository sessionRepository) {
        this.applicationRoleRepository = applicationRoleRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Jwt registerApplicationSession(RequireSessionDTO sessionDTO) {
        return null;
    }

    @Override
    public AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO) {
        String applicationHeader = ApplicationKeyUtility.getAppKey();

        List<PermissionAppDTO> permissionAppDTOList = applicationRoleRepository.findRoleByApplicationId(applicationHeader);

        //TODO RESOLVER PROBLEMA DE SAVE

        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setCredentialId(sessionDTO.getCredential());
        sessionEntity.setActive(false);
        sessionEntity.setDtInclusion(LocalDateTime.now());
        sessionEntity.setDtExpiration(LocalDateTime.now().plusMinutes(1));

        ApplicationRoleEntity applicationRole = new ApplicationRoleEntity();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationRole.setApplicationEntity(applicationEntity);

        sessionEntity.setApplicationRole(applicationRole);
        sessionEntity.getApplicationRole().getApplicationEntity().setApplicationName(applicationHeader);

        sessionEntity.setId(1L);

        sessionRepository.saveAndFlush(sessionEntity);

        return new AuthorizationDTO(getJwt(sessionDTO, permissionAppDTOList));
    }

    private static String getJwt(RequireSessionDTO sessionDTO, List<PermissionAppDTO> permissionAppDTOList) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        permissionAppDTOList.forEach(permissionAppDTO -> {
            claims.put(PermissionAppDTO.getUniqueKey(permissionAppDTO), permissionAppDTO.getPermissionName());
        });
        final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(sessionDTO.getCredential())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 )) // 1 minute
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
