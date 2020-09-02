package com.yzl.judgehost.dto;

import com.yzl.judgehost.core.enumeration.JudgeResultEnum;


/**
 * 单次判题的数据传输对象
 *
 * @author yuzhanglong
 * @date 2020-6-30 10:18:40
 */
public class SingleJudgeResultDTO {
    private String realTimeCost;
    private String memoryCost;
    private String cpuTimeCost;
    private Integer condition;
    private String stdInPath;
    private String stdOutPath;
    private String stdErrPath;
    private String message;

    /**
     * 设置message，根据condition的数值，
     * 利用枚举类型转换成message以备返回给前端
     *
     * @author yuzhanglong
     * @date 2020-6-29 22:54:47
     */
    public void setMessageWithCondition() {
        JudgeResultEnum type = JudgeResultEnum.toJudgeResultType(this.condition);
        this.message = type.getMessage();
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

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getStdInPath() {
        return stdInPath;
    }

    public void setStdInPath(String stdInPath) {
        this.stdInPath = stdInPath;
    }

    public String getStdOutPath() {
        return stdOutPath;
    }

    public void setStdOutPath(String stdOutPath) {
        this.stdOutPath = stdOutPath;
    }

    public String getStdErrPath() {
        return stdErrPath;
    }

    public void setStdErrPath(String stdErrPath) {
        this.stdErrPath = stdErrPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SingleJudgeResultDTO{" +
                "realTimeCost='" + realTimeCost + '\'' +
                ", memoryCost='" + memoryCost + '\'' +
                ", cpuTimeCost='" + cpuTimeCost + '\'' +
                ", condition=" + condition +
                ", stdInPath='" + stdInPath + '\'' +
                ", stdOutPath='" + stdOutPath + '\'' +
                ", stdErrPath='" + stdErrPath + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
