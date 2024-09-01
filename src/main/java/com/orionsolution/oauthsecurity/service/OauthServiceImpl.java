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

    public OauthServiceImpl(ApplicationRoleRepository applicationRoleRepository,
                            SessionRepository sessionRepository,
                            PermissionsRepository permissionsRepository) {
        this.applicationRoleRepository = applicationRoleRepository;
        this.sessionRepository = sessionRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public AuthorizationDTO refreshTokenAccess(RequireSessionDTO requireSessionDTO) {
        SessionRegister result = getSessionRegister(requireSessionDTO);
        return getAuthorizationDTO(requireSessionDTO, result , Boolean.FALSE);
    }

    private AuthorizationDTO getAuthorizationDTO(RequireSessionDTO requireSessionDTO, SessionRegister result, Boolean active) {
        // Save new session
        if (!result.session.getActive() && !active) {
            throw new BusinessException.HandlerException("Session is not active", HttpStatus.UNAUTHORIZED);
        }
        String appKey = result.session().getApplicationRole().getApplicationEntity().getApplicationId();
        SessionEntity sessionEntity =
                SessionEntity.getSessionEntity(requireSessionDTO, appKey, active, result.session().getId(), BigDecimal.TEN.longValue());
        sessionRepository.saveAndFlush(sessionEntity);
        return new AuthorizationDTO(JwtUtility.getJWT(requireSessionDTO, result.claims(), result.secretKey()));
    }

    @Override
    public AuthorizationDTO registerApplicationSession(RequireSessionDTO sessionDTO) {
        SessionRegister result = getSessionRegister(sessionDTO);
        // Validate permissions
        JwtUtility.validatePermissions(sessionDTO, result.claims(), result.permissionsList());
        // Save new session
        return getAuthorizationDTO(sessionDTO, result , Boolean.TRUE);
    }

    private SessionRegister getSessionRegister(RequireSessionDTO sessionDTO) {
        // Get authorization header
        String authorizationHeader = ApplicationKeyUtility.getAuthorization(sessionDTO);

        // Get session by credential
        List<SessionEntity> sessionList = sessionRepository.getByCredentialSessionWithLimit(sessionDTO.getCredential());

        // Validate if session exists
        SessionEntity session = sessionList.stream().findFirst().orElseThrow(
                () -> new BusinessException.HandlerException("Session not found", HttpStatus.UNAUTHORIZED));

        // Get permissions register in pre-session
        List<PermissionsEntity> permissionsList =
                permissionsRepository.getPermissionsEntityByRoleId(session.getApplicationRole().getRoleCode().getId());

        // Recover secret key
        SecretKey secretKey = JwtUtility.recoverSecretKey(sessionDTO, session, permissionsList);

        // Get claims
        Claims claims = JwtUtility.getClaims(secretKey, authorizationHeader);
        return new SessionRegister(session, permissionsList, secretKey, claims);
    }

    private record SessionRegister(SessionEntity session, List<PermissionsEntity> permissionsList, SecretKey secretKey,
                                   Claims claims) {
    }


    @Transactional
    @Override
    public AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO) {
        String appKey = ApplicationKeyUtility.getAppKey();
        List<PermissionAppDTO> permissionAppDTOList = applicationRoleRepository.findRoleByApplicationId(appKey);
        if (permissionAppDTOList != null && sessionDTO.getOrigin().equals(
                permissionAppDTOList.stream().map(PermissionAppDTO::getApplicationName).findAny().orElse(""))) {
            SessionEntity sessionEntity = SessionEntity.getSessionEntity(sessionDTO, appKey, Boolean.FALSE, BigDecimal.ONE.longValue(), BigDecimal.ONE.longValue());
            sessionRepository.save(sessionEntity);
            return new AuthorizationDTO(JwtUtility.getJWT(sessionDTO, permissionAppDTOList, appKey));
        }
        throw new BusinessException.HandlerException("Application not authorized", HttpStatus.UNAUTHORIZED);
    }

}
