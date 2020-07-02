package com.yzl.judgehost.core.enumerations;

import java.util.stream.Stream;

/**
 * @author yuzhanglong
 * @date 2020-7-2 18:50
 * @description 判题偏好相关枚举
 * 接口调用者应该根据判题/比赛的性质来选择判题的偏好
 * 正确的选择可以让做题者获得更好的反馈
 * 或者提高判题的效率
 */
public enum JudgePreferenceEnum {
    //ACM 模式,期间有一个点出现非ACCETPT的情况则结束判题
    ACM("ACM"),

    //OI 模式, 每一个点都会被评测
    OI("OI");

    private final String preference;

    public String getPreference() {
        return preference;
    }

    JudgePreferenceEnum(String preference) {
        this.preference = preference;
    }

    public static JudgePreferenceEnum toJudgePreference(String preference) {
        return Stream.of(JudgePreferenceEnum.values())
                .filter(c -> c.preference.equals(preference))
                .findAny()
                .orElse(null);
    }
}
