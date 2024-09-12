package com.orionsolution.oauthsecurity.utility;

import lombok.Getter;

public class ApplicationKeyUtility {

    @Getter
    private static String authorization;

    public static void setAuthorization(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            authorization = authorization.split(" ")[1];
        }
        ApplicationKeyUtility.authorization = authorization;
    }

}
