package com.yzl.judgehost.dto;

import com.yzl.judgehost.validators.JudgeConfigValidated;
import org.hibernate.validator.constraints.Length;

/**
 * @author yuzhanglong
 * @date 2020-6-26 10:26
 * @description 判题数据传输对象
 */

//@JudgeConfigValidated
public class JudgeDTO {
    private String submissionCode;
    @Length(min = 10, max = 10, message = "id长度错误")
    private String submissionId;
    private Integer realTimeLimit;
    private Integer memoryLimit;
    private Integer cpuTimeLimit;
    private Integer outputLimit;
    private String language;

    @Override
    public String toString() {
        return "JudgeDTO{" +
                "submissionCode='" + submissionCode + '\'' +
                ", submissionId='" + submissionId + '\'' +
                ", realTimeLimit='" + realTimeLimit + '\'' +
                ", memoryLimit='" + memoryLimit + '\'' +
                ", cpuTimeLimit='" + cpuTimeLimit + '\'' +
                ", outputLimit='" + outputLimit + '\'' +
                '}';
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubmissionCode() {
        return submissionCode;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getRealTimeLimit() {
        return realTimeLimit;
    }

    public void setRealTimeLimit(Integer realTimeLimit) {
        this.realTimeLimit = realTimeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Integer getCpuTimeLimit() {
        return cpuTimeLimit;
    }

    public void setCpuTimeLimit(Integer cpuTimeLimit) {
        this.cpuTimeLimit = cpuTimeLimit;
    }

    public Integer getOutputLimit() {
        return outputLimit;
    }

    public void setOutputLimit(Integer outputLimit) {
        this.outputLimit = outputLimit;
    }
}
