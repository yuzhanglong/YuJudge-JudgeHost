package com.yzl.judgehost.validators;

import com.yzl.judgehost.dto.JudgeDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yuzhanglong
 * @description 判题相关配置的验证器
 */
public class JudgeConfigValidator implements ConstraintValidator<JudgeConfigValidated, JudgeDTO> {

    @Override
    public boolean isValid(JudgeDTO judgeDTO, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
