package com.yzl.judgehost.dto;

import com.yzl.judgehost.core.enumeration.JudgeResultEnum;


/**
 * @author yuzhanglong
 * @date 2020-6-30 10:18:40
 * @description 单次判题的数据传输对象
 */
public class SingleJudgeResultDTO {
    private String realTimeCost;
    private String memoryCost;
    private String cpuTimeCost;
    private Integer condition;
    private String stdinPath;
    private String stdoutPath;
    private String stderrPath;
    private String message;

    /**
     * @author yuzhanglong
     * @date 2020-6-29 22:54:47
     * @description 设置message，根据condition的数值，
     * 利用枚举类型转换成message以备返回给前端
     */
    public void setMessageWithCondition() {
        JudgeResultEnum type = JudgeResultEnum.toJudgeResultType(this.condition);
        this.message = type.getMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRealTimeCost() {
        return realTimeCost;
    }

    public void setRealTimeCost(String realTimeCost) {
        this.realTimeCost = realTimeCost;
    }

    public String getMemoryCost() {
        return memoryCost;
    }

    public void setMemoryCost(String memoryCost) {
        this.memoryCost = memoryCost;
    }

    public String getCpuTimeCost() {
        return cpuTimeCost;
    }

    public void setCpuTimeCost(String cpuTimeCost) {
        this.cpuTimeCost = cpuTimeCost;
    }

    public Integer getCondition() {
        return condition;
    }

    public String getStdinPath() {
        return stdinPath;
    }

    public void setStdinPath(String stdinPath) {
        this.stdinPath = stdinPath;
    }

    public void setStdoutPath(String stdoutPath) {
        this.stdoutPath = stdoutPath;
    }

    public String getStderrPath() {
        return stderrPath;
    }

    public void setStderrPath(String stderrPath) {
        this.stderrPath = stderrPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageWithCondition(String message) {
        this.message = message;
    }


    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getStdoutPath() {
        return stdoutPath;
    }

    @Override
    public String toString() {
        return "SingleJudgeResultDTO{" +
                "realTimeCost='" + realTimeCost + '\'' +
                ", memoryCost='" + memoryCost + '\'' +
                ", cpuTimeCost='" + cpuTimeCost + '\'' +
                ", condition=" + condition +
                ", stdinPath='" + stdinPath + '\'' +
                ", stdoutPath='" + stdoutPath + '\'' +
                ", stderrPath='" + stderrPath + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
