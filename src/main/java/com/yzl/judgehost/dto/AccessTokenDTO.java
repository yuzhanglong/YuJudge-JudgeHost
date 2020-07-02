package com.yzl.judgehost.dto;

import com.yzl.judgehost.validators.LoginValidated;

import javax.validation.constraints.NotNull;

/**
 * @author yuzhanglong
 * @description accesstoken数据传输对象
 * @date 2020-7-2 00:35
 */
@LoginValidated
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
