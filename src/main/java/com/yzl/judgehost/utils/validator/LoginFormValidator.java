package com.yzl.judgehost.utils.validator;

import com.yzl.judgehost.dto.AccessTokenDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 登录验证
 *
 * @author yuzhanglong
 * @date 2020-8-22 00:39:07
 */
public class LoginFormValidator implements ConstraintValidator<LoginFormValidated, AccessTokenDTO> {
    @Override
    public void initialize(LoginFormValidated constraintAnnotation) {

    }


    @Override
    public boolean isValid(AccessTokenDTO accessTokenDTO, ConstraintValidatorContext constraintValidatorContext) {
        String test1 = accessTokenDTO.getAccessToken();
        return false;
    }
}
