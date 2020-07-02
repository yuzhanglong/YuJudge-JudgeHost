package com.yzl.judgehost.vo;

/**
 * @author yuzhanglong
 * @description 对某次鉴权结果的表现层对象
 * @date 2020-7-1 23:37
 */
public class AuthorizationVO {
    private String accessToken;
    private Integer expiresIn;

    public AuthorizationVO(String accessToken, Integer expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AuthorizationVO{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }
}
