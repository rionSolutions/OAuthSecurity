package com.orionsolution.oauthsecurity.model;

import lombok.Data;

@Data
public class RequireSessionDTO {
    private String credential;
    private String roleClient;
    private String origin;
}
