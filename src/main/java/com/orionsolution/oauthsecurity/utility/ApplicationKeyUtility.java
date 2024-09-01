package com.orionsolution.oauthsecurity.utility;

import com.orionsolution.oauthsecurity.model.RequireSessionDTO;
import lombok.Getter;

public class ApplicationKeyUtility {
    @Getter
    private static String appKey;
    @Getter
    private static String authorization;

    public static void setAppKey(String appKey) {
        ApplicationKeyUtility.appKey = appKey;
    }

    public static void setAuthorization(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            authorization = authorization.split(" ")[1];
        }
        ApplicationKeyUtility.authorization = authorization;
    }

    public static String getAuthorization(RequireSessionDTO requireSessionDTO) {
        if (requireSessionDTO.getToken() != null) {
            return requireSessionDTO.getToken();
        }
        return authorization;
    }
}
