package com.yzl.judgehost.validators;

import com.yzl.judgehost.dto.AuthorizationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yuzhanglong
 */
public class LoginValidator implements ConstraintValidator<LoginValidated, AuthorizationDTO> {
    @Override
    public void initialize(LoginValidated constraintAnnotation) {

    }


    @Override
    public boolean isValid(AuthorizationDTO authorizationDTO, ConstraintValidatorContext constraintValidatorContext) {
        String test1 = authorizationDTO.getToken();
        return false;
    }
}
