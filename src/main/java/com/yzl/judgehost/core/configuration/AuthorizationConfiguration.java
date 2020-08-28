package com.yzl.judgehost.core.configuration;

import com.yzl.judgehost.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 权限相关配置类
 *
 * @author yuzhanglong
 * @date 2020-7-1 23:44
 */

@Component
@ConfigurationProperties(prefix = "judge-authorization")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class AuthorizationConfiguration {
    private String userId;
    private String userSecret;
    private Integer expiredIn;
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


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

    public Integer getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(Integer expiredIn) {
        this.expiredIn = expiredIn;
    }
}
