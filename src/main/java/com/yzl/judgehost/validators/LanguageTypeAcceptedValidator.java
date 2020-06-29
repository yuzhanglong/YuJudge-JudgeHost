package com.yzl.judgehost.validators;

import com.yzl.judgehost.core.enumerations.LanguageScriptEnum;
import com.yzl.judgehost.dto.JudgeDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author yuzhanglong
 * @description 语言类型验证
 * @date 2020-6-28 15:01
 */
public class LanguageTypeAcceptedValidator implements ConstraintValidator<LanguageTypeAccepted, JudgeDTO> {
    @Override
    public boolean isValid(JudgeDTO judgeDTO, ConstraintValidatorContext constraintValidatorContext) {
        String language = judgeDTO.getLanguage();
        return LanguageScriptEnum.isLanguageAccepted(language);
    }
}
