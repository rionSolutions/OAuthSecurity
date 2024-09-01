package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.entity.ApplicationRoleEntity;
import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import com.orionsolution.oauthsecurity.entity.RolesEntity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OauthServiceImplTest {

    @Mock
    private ApplicationRoleRepository applicationRoleRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PermissionsRepository permissionsRepository;

    @InjectMocks
    private OauthServiceImpl oauthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerApplicationSession_validSession_returnsAuthorizationDTO() {
        RequireSessionDTO sessionDTO = new RequireSessionDTO();
        sessionDTO.setCredential("validCredential");
        String authorizationHeader = "validAuthorizationHeader";
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setApplicationRole(new ApplicationRoleEntity());
        sessionEntity.getApplicationRole().setRoleCode(new RolesEntity());
        sessionEntity.getApplicationRole().getRoleCode().setId(1L);
        List<SessionEntity> sessionList = Collections.singletonList(sessionEntity);
        List<PermissionsEntity> permissionsEntityList = Collections.singletonList(new PermissionsEntity());
        SecretKey secretKey = Keys.hmacShaKeyFor("secret".getBytes());
        Claims claims = Jwts.claims();

        when(ApplicationKeyUtility.getAuthorization()).thenReturn(authorizationHeader);
        when(sessionRepository.getByCredentialSessionWithLimit(anyString())).thenReturn(sessionList);
        when(permissionsRepository.getPermissionsEntityByRoleId(anyLong())).thenReturn(permissionsEntityList);
        when(JwtUtility.recoverSecretKey(any(), any(), any())).thenReturn(secretKey);
        when(JwtUtility.getClaims(any(), anyString())).thenReturn(claims);

        AuthorizationDTO result = oauthService.registerApplicationSession(sessionDTO);

        assertNotNull(result);
    }

    @Test
    void registerApplicationSession_invalidSession_throwsBusinessException() {
        RequireSessionDTO sessionDTO = new RequireSessionDTO();
        sessionDTO.setCredential("invalidCredential");

        when(sessionRepository.getByCredentialSessionWithLimit(anyString())).thenReturn(Collections.emptyList());

        BusinessException.HandlerException exception = assertThrows(BusinessException.HandlerException.class, () -> {
            oauthService.registerApplicationSession(sessionDTO);
        });

        //assertEquals("Session not found", exception.getMessage());
       // assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void requestAuthorization_validRequest_returnsAuthorizationDTO() {
        RequireSessionDTO sessionDTO = new RequireSessionDTO();
        sessionDTO.setOrigin("validOrigin");
        String appKey = "validAppKey";
        List<PermissionAppDTO> permissionAppDTOList = Collections.singletonList(
                new PermissionAppDTO("", 1L , "" , "" , 1L));
        permissionAppDTOList.get(0).setApplicationName("validOrigin");

        when(ApplicationKeyUtility.getAppKey()).thenReturn(appKey);
        when(applicationRoleRepository.findRoleByApplicationId(anyString())).thenReturn(permissionAppDTOList);

        AuthorizationDTO result = oauthService.requestAuthorization(sessionDTO);

        assertNotNull(result);
    }


}