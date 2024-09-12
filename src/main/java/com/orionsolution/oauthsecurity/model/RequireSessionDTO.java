package com.orionsolution.oauthsecurity.model;

import lombok.Data;

import java.util.Map;

@Data
public class RequireSessionDTO {
    private String session_id;
    private String token;
    private String required;
    private String client_secret;
    private String client_id;

    public RequireSessionDTO(Map<String,String> formParams ) {
        formParams.forEach((key, value) -> {
            switch (key) {
                case "token":
                    this.token = value;
                    break;
                case "required":
                    this.required = value;
                    break;
                case "client_secret":
                    this.client_secret = value;
                    break;
                case "client_id":
                    this.client_id = value;
                    break;
            }
        });
    }
}
