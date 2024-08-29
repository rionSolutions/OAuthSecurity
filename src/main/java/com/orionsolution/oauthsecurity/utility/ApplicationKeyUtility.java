package com.orionsolution.oauthsecurity.utility;

import lombok.Getter;

public class ApplicationKeyUtility {
    @Getter
    private static String appKey;

    public static void setAppKey(String appKey) {
        ApplicationKeyUtility.appKey = appKey;
    }
}
