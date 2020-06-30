package com.yzl.judgehost.core.enumerations;

import java.util.stream.Stream;

/**
 * @author yuzhanglong
 * @date 2020-6-29 22:54:47
 * @description 一次判题结果的枚举类
 */

public enum JudgeResultEnum {
    // 程序通过这个测试样例
    ACCEPTED("ACCEPT", 0),

    // 答案错误，一般是程序正常运行，但是和期望输出不匹配
    WRONG_ANSWER("WRONG_ANSWER", 1),

    // 运行时错误
    RUNTIME_ERROR("RUNTIME_ERROR", 2),

    // 时间超限
    TIME_LIMIT_EXCEEDED("TIME_LIMIT_EXCEEDED", 3),

    // 内存超限
    MEMORY_LIMIT_EXCEED("MEMORY_LIMIT_EXCEED", 4),

    // 输出超限
    OUTPUT_LIMIT_EXCEED("OUTPUT_LIMIT_EXCEED", 5),

    // 段错误
    SEGMENTATION_FAULT("SEGMENTATION_FAULT", 6),

    // 浮点错误
    FLOAT_ERROR("FLOAT_ERROR", 7),

    // 未知错误
    UNKNOWN_ERROR("UNKNOWN_ERROR", 8),

    // 找不到输入文件
    INPUT_FILE_NOT_FOUND("INPUT_FILE_NOT_FOUND", 9),

    // 无法寻找输出
    CAN_NOT_MAKE_OUTPUT("CAN_NOT_MAKE_OUTPUT", 10),

    // 设置限制错误
    SET_LIMIT_ERROR("SET_LIMIT_ERROR", 11),

    // 非管理员用户
    UNROOT_USER("UNROOT_USER", 12),

    //fork失败
    FORK_ERROR("FORK_ERROR", 13),

    //监控线程创建失败
    CREATE_THREAD_ERROR("CREATE_THREAD_ERROR", 14),

    //参数校验失败
    VALIDATE_ERROR("VALIDATE_ERROR", 15),

    //编译失败
    COMPILE_ERROR("COMPILE_ERROR", 16);

    public String getMessage() {
        return message;
    }

    public Integer getNumber() {
        return number;
    }

    private final String message;
    private final Integer number;

    JudgeResultEnum(String message, Integer number) {
        this.message = message;
        this.number = number;
    }

    public static JudgeResultEnum toJudgeResultType(Integer number) {
        return Stream.of(JudgeResultEnum.values())
                .filter(c -> c.number.equals(number))
                .findAny()
                .orElse(null);
    }
}
