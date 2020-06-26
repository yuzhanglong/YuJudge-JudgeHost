package com.yzl.judgehost.validators;

import com.auth0.jwt.interfaces.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author yuzhanglong
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = LoginValidator.class)
public @interface LoginValidated {
    String message() default "bad validate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}