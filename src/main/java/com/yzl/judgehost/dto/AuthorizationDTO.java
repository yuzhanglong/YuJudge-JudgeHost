package com.yzl.judgehost.dto;

import javax.validation.constraints.NotNull;

/**
 * 用户权限认证的数据传输对象
 *
 * @author yuzhanglong
 * @date 2020-7-1 23:17
 */
public class AuthorizationDTO {
    @NotNull(message = "用户id不得为空")
    private String userId;
    @NotNull(message = "用户secret不得为空")
    private String userSecret;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "userId='" + userId + '\'' +
                ", userSecret='" + userSecret + '\'' +
                '}';
    }
}
