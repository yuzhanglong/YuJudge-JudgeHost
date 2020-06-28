package com.yzl.judgehost.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author yuzhanglong
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = JudgeConfigValidator.class)
public @interface JudgeConfigValidated {
    String message() default "Judgeconfig is not validated!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
