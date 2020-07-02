package com.yzl.judgehost.core.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuzhanglong
 * @description 标示请求是否需要认证的注解
 * @date 2020-7-2 8:38
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationRequired {
}
