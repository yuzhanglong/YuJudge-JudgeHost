package com.yzl.judgehost.utils.validator;

import com.yzl.judgehost.core.enumeration.LanguageScriptEnum;
import com.yzl.judgehost.dto.JudgeDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 语言类型验证
 *
 * @author yuzhanglong
 * @date 2020-6-28 15:01
 */
public class LanguageTypeAcceptedValidator implements ConstraintValidator<LanguageTypeAccepted, JudgeDTO> {
    @Override
    public boolean isValid(JudgeDTO judgeDTO, ConstraintValidatorContext constraintValidatorContext) {
        String language = judgeDTO.getLanguage();
        return LanguageScriptEnum.isLanguageAccepted(language);
    }
}
