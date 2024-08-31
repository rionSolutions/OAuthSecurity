package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import com.orionsolution.oauthsecurity.entity.SessionEntity;
import com.orionsolution.oauthsecurity.exception.BusinessException;
import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.PermissionAppDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import com.orionsolution.oauthsecurity.repository.ApplicationRoleRepository;
import com.orionsolution.oauthsecurity.repository.PermissionsRepository;
import com.orionsolution.oauthsecurity.repository.SessionRepository;
import com.orionsolution.oauthsecurity.utility.ApplicationKeyUtility;
import com.orionsolution.oauthsecurity.utility.JwtUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final ApplicationRoleRepository applicationRoleRepository;
    private final SessionRepository sessionRepository;
    private final PermissionsRepository permissionsRepository;

    public OauthServiceImpl(ApplicationRoleRepository applicationRoleRepository, SessionRepository sessionRepository, PermissionsRepository permissionsRepository) {
        this.applicationRoleRepository = applicationRoleRepository;
        this.sessionRepository = sessionRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public AuthorizationDTO registerApplicationSession(RequireSessionDTO sessionDTO) {
        String authorizationHeader = ApplicationKeyUtility.getAuthorization();
        List<SessionEntity> sessionList = sessionRepository.getByCredentialSessionWithLimit(sessionDTO.getCredential());
        SessionEntity session = sessionList.stream().findFirst().orElseThrow(
                () -> new BusinessException.HandlerException("Session not found", HttpStatus.UNAUTHORIZED));

        List<PermissionsEntity> permissionsEntityList =
                permissionsRepository.getPermissionsEntityByRoleId(session.getApplicationRole().getRoleCode().getId());

        SecretKey secretKey = JwtUtility.recoverSecretKey(sessionDTO, session, permissionsEntityList);
        Claims claims = JwtUtility.getClaims(secretKey, authorizationHeader);
        String appKey = session.getApplicationRole().getApplicationEntity().getApplicationId();

        SessionEntity sessionEntity =
                SessionEntity.getSessionEntity(sessionDTO, appKey, Boolean.TRUE, session.getId());

        sessionRepository.saveAndFlush(sessionEntity);

        return new AuthorizationDTO(JwtUtility.getJWT(sessionDTO, claims, secretKey));
    }


    @Transactional
    @Override
    public AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO) {
        String appKey = ApplicationKeyUtility.getAppKey();
        List<PermissionAppDTO> permissionAppDTOList = applicationRoleRepository.findRoleByApplicationId(appKey);
        if (permissionAppDTOList != null && sessionDTO.getOrigin().equals(
                permissionAppDTOList.stream().map(PermissionAppDTO::getApplicationName).findAny().orElse(""))) {
            SessionEntity sessionEntity = SessionEntity.getSessionEntity(sessionDTO, appKey, Boolean.FALSE, BigDecimal.ONE.longValue());
            sessionRepository.save(sessionEntity);
            return new AuthorizationDTO(JwtUtility.getJWT(sessionDTO, permissionAppDTOList, appKey));
        }
        throw new BusinessException.HandlerException("Application not authorized", HttpStatus.UNAUTHORIZED);
    }

}
