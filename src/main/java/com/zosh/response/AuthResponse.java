package com.zosh.response;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private boolean status;
    private String message;
    private boolean isTwoFactorAuthenticationEnabled;
    private String session;
}
