package com.yzl.judgehost.core.enumerations;

/**
 * @author yuzhanglong
 * @description 判题配置枚举类
 * 一些判题配置的默认值，如果用户没有传入，则会使用这些默认值
 */

public enum JudgeConfigEnum {
    // cpu时间限制
    TIME_LIMIT_DEFAULT(4),

    // 内存限制 32mb
    MEMORY_LIMIT_DEFAULT(1024 * 1024 * 32),

    // 时间限制
    WALL_TIME_DEFAULT(4),

    // 进程限制
    PROCESS_LIMIT_DEFAULT(1),

    // 输出限制
    OUTPUT_LIMIT_DEFAULT(100000000);

    private final Integer data;

    JudgeConfigEnum(Integer data) {
        this.data = data;
    }

    public Integer getData() {
        return data;
    }
}
