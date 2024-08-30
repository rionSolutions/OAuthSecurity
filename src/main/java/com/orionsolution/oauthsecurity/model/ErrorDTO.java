package com.orionsolution.oauthsecurity.model;

public record ErrorDTO(
        String message,
        int status
) {
}
