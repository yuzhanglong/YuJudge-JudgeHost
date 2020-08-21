package com.yzl.judgehost.dto;

import com.yzl.judgehost.utils.validator.LoginFormValidated;

import javax.validation.constraints.NotNull;

/**
 * AccessToken数据传输对象
 *
 * @author yuzhanglong
 * @date 2020-7-2 00:35
 */
@LoginFormValidated
public class AccessTokenDTO {
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    @NotNull
    private String accessToken;

    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "token='" + accessToken + '\'' +
                '}';
    }
}
