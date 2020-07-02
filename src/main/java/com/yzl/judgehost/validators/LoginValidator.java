package com.yzl.judgehost.validators;

import com.yzl.judgehost.dto.AccessTokenDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yuzhanglong
 */
public class LoginValidator implements ConstraintValidator<LoginValidated, AccessTokenDTO> {
    @Override
    public void initialize(LoginValidated constraintAnnotation) {

    }


    @Override
    public boolean isValid(AccessTokenDTO accessTokenDTO, ConstraintValidatorContext constraintValidatorContext) {
        String test1 = accessTokenDTO.getAccessToken();
        return false;
    }
}
