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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.orionsolution.oauthsecurity.utility.JwtUtility.recoverSecretKey;

/**
 * OauthServiceImpl provides the implementation for OAuth-related operations such as token refresh, session registration, and authorization.
 */
@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final ApplicationRoleRepository applicationRoleRepository;
    private final SessionRepository sessionRepository;
    private final PermissionsRepository permissionsRepository;

    /**
     * Constructs an OauthServiceImpl with the specified repositories.
     *
     * @param applicationRoleRepository the repository for application roles
     * @param sessionRepository the repository for sessions
     * @param permissionsRepository the repository for permissions
     */
    public OauthServiceImpl(ApplicationRoleRepository applicationRoleRepository,
                            SessionRepository sessionRepository,
                            PermissionsRepository permissionsRepository) {
        this.applicationRoleRepository = applicationRoleRepository;
        this.sessionRepository = sessionRepository;
        this.permissionsRepository = permissionsRepository;
    }

    /**
     * Refreshes the token access based on the provided session details.
     *
     * @param requireSessionDTO the session details required for refreshing the token
     * @return an AuthorizationDTO containing the new token
     */
    @Override
    public AuthorizationDTO refreshTokenAccess(@NotNull RequireSessionDTO requireSessionDTO) {
        SessionRegister result = getSessionRegister(ApplicationKeyUtility.getAuthorization());
        return getAuthorizationDTO(requireSessionDTO.getClient_secret(), String.valueOf(result.session.getId()), result, true);
    }

    /**
     * Constructs an AuthorizationDTO based on the provided parameters.
     *
     * @param client_secret the client secret
     * @param session_id the session ID
     * @param result the session register result
     * @param isRefresh flag indicating if this is a token refresh operation
     * @return a new AuthorizationDTO
     */
    @Contract("_, _, _, _ -> new")
    private @NotNull AuthorizationDTO getAuthorizationDTO(String client_secret,
                                                          String session_id,
                                                          @NotNull SessionRegister result,
                                                          Boolean isRefresh) {
        if (!result.session.getActive() && isRefresh) {
            throw new BusinessException.HandlerException("Session is not active", HttpStatus.UNAUTHORIZED);
        }

        SessionEntity sessionEntity =
                SessionEntity.getSessionEntity(client_secret, true , result.session().getId(), BigDecimal.TEN.longValue());
        sessionRepository.saveAndFlush(sessionEntity);

        return new AuthorizationDTO(JwtUtility.getJWT(session_id, result.claims(), result.secretKey()));
    }

    /**
     * Registers an application session based on the system origin.
     *
     * @param systemOrigin the origin of the system making the request
     * @return an AuthorizationDTO containing the session details
     */
    public AuthorizationDTO registerApplicationSession(String systemOrigin) {
        SessionRegister result = getSessionRegister(ApplicationKeyUtility.getAuthorization());

        JwtUtility.validatePermissions(result.claims(), result.permissionsList(), systemOrigin);

        return getAuthorizationDTO(
                result.session.getApplicationRole().getApplicationEntity().getApplicationId(),
                result.session.getId().toString(),
                result,
                false);
    }

    /**
     * Retrieves the session register based on the provided token.
     *
     * @param token the JWT token
     * @return a new SessionRegister containing session details
     */
    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_ -> new")
    private SessionRegister getSessionRegister(String token) {
        String subject = JwtUtility.getSubjectFromToken(token);
        Optional<SessionEntity> session = sessionRepository.getByIdSessionWithLimit(subject);

        if (session.isEmpty()) {
            throw new BusinessException.HandlerException("Session not found", HttpStatus.UNAUTHORIZED);
        }

        SessionEntity sessionEntity = session.get();
        List<PermissionsEntity> permissionsList =
                permissionsRepository.getPermissionsEntityByRoleId(sessionEntity.getApplicationRole().getRoleCode().getId());
        SecretKey secretKey = recoverSecretKey(sessionEntity, permissionsList);
        Claims claims = JwtUtility.getClaims(secretKey, token);
        return new SessionRegister(sessionEntity, permissionsList, secretKey, claims);
    }

    /**
     * A record to hold session register details.
     */
    private record SessionRegister(SessionEntity session, List<PermissionsEntity> permissionsList, SecretKey secretKey,
                                   Claims claims) {
    }

    /**
     * Requests authorization based on the provided session details.
     *
     * @param sessionDTO the session details required for authorization
     * @return an AuthorizationDTO containing the authorization details
     */
    @Transactional
    @Override
    public AuthorizationDTO requestAuthorization(@NotNull RequireSessionDTO sessionDTO) {
        List<PermissionAppDTO> permissionAppDTOList = applicationRoleRepository.findRoleByApplicationId(sessionDTO.getClient_secret());
        if (permissionAppDTOList != null && sessionDTO.getClient_id().equals(
                permissionAppDTOList.stream().map(PermissionAppDTO::getApplicationName).findAny().orElse(""))) {
            SessionEntity sessionEntity =
                    SessionEntity.getSessionEntity(
                            sessionDTO.getClient_secret(), Boolean.FALSE, BigDecimal.ONE.longValue(), BigDecimal.ONE.longValue()
                    );
            SessionEntity session = sessionRepository.save(sessionEntity);
            sessionDTO.setSession_id(session.getId().toString());

            return new AuthorizationDTO(JwtUtility.getJWT(sessionDTO, permissionAppDTOList));
        }
        throw new BusinessException.HandlerException("Application not authorized", HttpStatus.UNAUTHORIZED);
    }

}