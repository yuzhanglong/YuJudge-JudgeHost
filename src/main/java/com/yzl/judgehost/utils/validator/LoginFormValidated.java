package com.yzl.judgehost.utils.validator;

import com.auth0.jwt.interfaces.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 登录验证
 *
 * @author yuzhanglong
 * @date 2020-8-22 00:38:56
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = LoginFormValidator.class)
public @interface LoginFormValidated {
    String message() default "bad validate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}