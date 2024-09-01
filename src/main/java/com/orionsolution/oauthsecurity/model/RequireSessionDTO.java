package com.orionsolution.oauthsecurity.model;

import lombok.Data;

@Data
public class RequireSessionDTO {
    private String token;
    private String credential;
    private String origin;
    private String required;
}
