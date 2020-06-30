package com.yzl.judgehost.dto;

import com.yzl.judgehost.validators.LanguageTypeAccepted;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yuzhanglong
 * @date 2020-6-26 10:26
 * @description 判题数据传输对象
 */
@LanguageTypeAccepted
public class JudgeDTO {
    @NotNull(message = "代码不得为空")
    private String submissionCode;

    @DecimalMax(value = "10", message = "实际时间限制最大为10s")
    private Integer realTimeLimit;

    @DecimalMax(value = "10", message = "cpu时间限制最大为10s")
    private Integer cpuTimeLimit;

    @DecimalMin(value = "10", message = "cpu时间限制最小为200kb")
    private Integer memoryLimit;

    @DecimalMin(value = "10", message = "输出限制最小为1000")
    @DecimalMax(value = "1000000", message = "输出限制最大为1000000")
    private Integer outputLimit;


    @NotNull(message = "语言不得为空")
    private String language;


    @NotNull(message = "期望输入、输出不得为空")
    @Size(message = "期望输入、输出长度最小为1、最大为7", min = 1, max = 10)
    @Valid
    private List<ResolutionDTO> resolutions;

    @Override
    public String toString() {
        return "JudgeDTO{" +
                "submissionCode='" + submissionCode + '\'' +
                ", realTimeLimit=" + realTimeLimit +
                ", cpuTimeLimit=" + cpuTimeLimit +
                ", memoryLimit=" + memoryLimit +
                ", outputLimit=" + outputLimit +
                ", language='" + language + '\'' +
                ", resolutions=" + resolutions +
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

    public List<ResolutionDTO> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<ResolutionDTO> resolutions) {
        this.resolutions = resolutions;
    }
}
