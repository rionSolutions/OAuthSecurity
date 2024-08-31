package com.orionsolution.oauthsecurity.exception;

public record Error(
        String message,
        int status
) {
}
