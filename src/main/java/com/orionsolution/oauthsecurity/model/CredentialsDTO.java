package com.orionsolution.oauthsecurity.model;

import lombok.Data;

@Data
public class CredentialsDTO {
    private String credential;
    private String secretKey;
}
