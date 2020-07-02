package com.yzl.judgehost.dto;

import javax.validation.constraints.NotNull;

/**
 * @author yuzhanglong
 * @description 解决方案数据传输对象
 * 用户需要传入一个或多个解决方案，每一个解决方案包括：
 * 1. 输入文件 stdin --- input
 * 2. 输出文件 stdout --- expectedOutput
 * 例如：
 * {
 * "input": "your_download_path",
 * "expectedOutput":"your_download_path"
 * }
 * @date 2020-6-29 18:16:19
 */

public class ResolutionDTO {
    @NotNull(message = "输入不得为空")
    String input;
    @NotNull(message = "期望输出不得为空")
    String expectedOutput;


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }


    @Override
    public String toString() {
        return "ResolutionDTO{" +
                "input='" + input + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                '}';
    }
}
