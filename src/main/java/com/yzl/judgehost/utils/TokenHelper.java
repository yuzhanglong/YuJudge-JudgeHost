package com.yzl.judgehost.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuzhanglong
 * @date 2020-6-25 11:17
 * @description token生成的工具类
 */

@Component
public class TokenHelper {
    @Value("${judge-authorization.secret-key}")
    public void setSecretKey(String secretKey) {
        //tip: 不应该通过类实例访问静态成员
        TokenHelper.secretKey = secretKey;
    }

    @Value("${judge-authorization.expired-time}")
    public void setExpiredTime(Integer expiredTime) {
        TokenHelper.expiredTime = expiredTime;
    }

    private static String secretKey;
    private static Integer expiredTime;

    /**
     * @param uid 用户id
     * @return String 生成的token
     * @author yuzhanglong
     * @description 传入userid。生成对应的token
     */
    public static String generateAuthToken(String uid) {
        Algorithm algorithm = Algorithm.HMAC256(TokenHelper.secretKey);
        Map<String, Date> map = calculateExpiredInfo();
        return JWT.create()
                .withClaim("uid", uid)
                .withExpiresAt(map.get("expiredTime"))
                .withIssuedAt(map.get("now"))
                .sign(algorithm);
    }

    /**
     * @return map 返回一系列kv对，具体内容查看 @description
     * @author yuzhanglong
     * @description 处理token中时间相关信息，这包括：
     * now: 当前时间
     * expiredTime: 过期时间
     */
    public static Map<String, Date> calculateExpiredInfo() {
        Map<String, Date> map = new HashMap<>(2);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, TokenHelper.expiredTime);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }

    /**
     * @param token 用户传入的token
     * @return boolean token是否验证通过
     * @author yuzhanglong
     * @description 检测传入的token是否合法、正确
     */
    public static Boolean checkAuthToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TokenHelper.secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
