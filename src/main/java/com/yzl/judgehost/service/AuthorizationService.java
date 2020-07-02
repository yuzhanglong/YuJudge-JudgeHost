package com.yzl.judgehost.service;

import com.yzl.judgehost.core.configuration.AuthorizationConfiguration;
import com.yzl.judgehost.dto.AuthorizationDTO;
import com.yzl.judgehost.dto.AccessTokenDTO;
import com.yzl.judgehost.exception.http.ForbiddenException;
import com.yzl.judgehost.utils.TokenHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author yuzhanglong
 * @description 权限、认证相关业务模块
 * @date 2020-7-1 23:31
 */

@Service
public class AuthorizationService {
    private final AuthorizationConfiguration authorizationConfiguration;

    public AuthorizationService(AuthorizationConfiguration authorizationConfiguration) {
        this.authorizationConfiguration = authorizationConfiguration;
    }


    /**
     * @param authorizationDTO 用户权限认证的数据传输对象
     * @return String access_token
     * @author yuzhanglong
     * @description 获取判题服务器接口调用凭据
     * @date 2020-7-1 23:31
     */
    public String getAccessToken(AuthorizationDTO authorizationDTO) {
        String userId = authorizationDTO.getUserId();
        Boolean isPass = isUserSecretChecked(userId, authorizationDTO.getUserSecret());
        if (!isPass) {
            throw new ForbiddenException("A0003");
        }
        Integer expiredIn = authorizationConfiguration.getExpiredIn();
        String secretKey = authorizationConfiguration.getSecretKey();
        return TokenHelper.generateAuthToken(userId, secretKey, expiredIn);
    }

    /**
     * @param accessTokenDTO token的数据传输对象
     * @return Boolean 验证是否通过
     * @author yuzhanglong
     * @description 验证接口调用凭据合法性
     * @date 2020-7-2 00:26
     */
    public Boolean checkAccessToken(@Validated AccessTokenDTO accessTokenDTO) {
        String accessToken = accessTokenDTO.getAccessToken();
        String userId = authorizationConfiguration.getUserId();
        String secretKey = authorizationConfiguration.getSecretKey();
        return TokenHelper.checkAuthToken(accessToken, userId, secretKey);
    }

    /**
     * @param userId     用户名
     * @param userSecret 用户密码
     * @return Boolean 验证用户名或密码是否正确
     * @author yuzhanglong
     * @description 验证接口调用凭据合法性
     * 由于此服务器(judgehost)专门用来执行判题，个人觉得无需在额外搞一个数据库用来存储用户信息
     * 另外需要存储的数据也不多（可能就是用户名和密码了）
     * 规定的用户名和密码来自文件，你可以在配置文件中修改之（注意区分开发环境和生产环境）
     * @date 2020-7-2 00:33
     */
    private Boolean isUserSecretChecked(String userId, String userSecret) {
        return userId.equals(getUserId()) && userSecret.equals(getUserSecret());
    }

    private String getUserId() {
        return authorizationConfiguration.getUserId();
    }

    private String getUserSecret() {
        return authorizationConfiguration.getUserSecret();
    }

    public Integer getExpiredTime() {
        return authorizationConfiguration.getExpiredIn();
    }
}
