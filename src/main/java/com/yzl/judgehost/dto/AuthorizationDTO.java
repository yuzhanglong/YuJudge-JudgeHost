package com.yzl.judgehost.dto;

import com.yzl.judgehost.validators.LoginValidated;

/**
 * @author yuzhanglong
 */
@LoginValidated
public class AuthorizationDTO {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "token='" + token + '\'' +
                '}';
    }
}
