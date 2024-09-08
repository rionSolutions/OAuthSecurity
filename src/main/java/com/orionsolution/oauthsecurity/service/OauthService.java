package com.orionsolution.oauthsecurity.service;

import com.orionsolution.oauthsecurity.model.AuthorizationDTO;
import com.orionsolution.oauthsecurity.model.RequireSessionDTO;

/**
 * OauthService defines the contract for OAuth-related operations such as token refresh, session registration, and authorization.
 */
public interface OauthService {

    /**
     * Refreshes the token access based on the provided session details.
     *
     * @param requireSessionDTO the session details required for refreshing the token
     * @return an AuthorizationDTO containing the new token
     */
    AuthorizationDTO refreshTokenAccess(RequireSessionDTO requireSessionDTO);

    /**
     * Registers an application session based on the system origin.
     *
     * @param systemOrigin the origin of the system making the request
     * @return an AuthorizationDTO containing the session details
     */
    AuthorizationDTO registerApplicationSession(String systemOrigin);

    /**
     * Requests authorization based on the provided session details.
     *
     * @param sessionDTO the session details required for authorization
     * @return an AuthorizationDTO containing the authorization details
     */
    AuthorizationDTO requestAuthorization(RequireSessionDTO sessionDTO);
}