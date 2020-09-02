package com.yzl.judgehost.dto;

import javax.validation.constraints.NotNull;

/**
 * 解决方案数据传输对象
 * 用户需要传入一个或多个解决方案，每一个解决方案包括：
 * 1. 输入文件 stdin --- stdIn
 * 2. 输出文件 stdout --- expectedStdOut
 * 例如：
 * {
 * "stdIn": "your_download_path",
 * "expectedStdOut":"your_download_path"
 * }
 *
 * @author yuzhanglong
 * @date 2020-6-29 18:16:19
 */

public class SolutionDTO {
    @NotNull(message = "输入不得为空")
    String stdIn;
    @NotNull(message = "期望输出不得为空")
    String expectedStdOut;


    public String getStdIn() {
        return stdIn;
    }

    public void setStdIn(String stdIn) {
        this.stdIn = stdIn;
    }

    public String getExpectedStdOut() {
        return expectedStdOut;
    }

    public void setExpectedStdOut(String expectedStdOut) {
        this.expectedStdOut = expectedStdOut;
    }


    @Override
    public String toString() {
        return "ResolutionDTO{" +
                "stdIn='" + stdIn + '\'' +
                ", expectedStdOut='" + expectedStdOut + '\'' +
                '}';
    }
}
