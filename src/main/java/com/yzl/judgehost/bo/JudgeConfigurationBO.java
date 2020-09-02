package com.yzl.judgehost.bo;

import com.yzl.judgehost.dto.JudgeDTO;

import java.util.List;
import java.util.UUID;

/**
 * 判题配置，可以理解为全局变量
 *
 * @author yuzhanglong
 * @date 2020-09-01 20:55:03
 */
public class JudgeConfigurationBO {
    private String submissionPath;
    private String submissionId;
    private List<String> extraInfo;
    private JudgeDTO judgeConfig;
    private Runtime runner;
    private String workPath;
    private String scriptPath;
    private String resolutionPath;

    public JudgeConfigurationBO(
            JudgeDTO judgeConfig,
            String workPath,
            String scriptPath,
            String resolutionPath) {
        this.submissionId = UUID.randomUUID().toString();
        this.judgeConfig = judgeConfig;
        this.runner = Runtime.getRuntime();
        this.workPath = workPath;
        this.scriptPath = scriptPath;
        this.resolutionPath = resolutionPath;
    }

    public String getSubmissionPath() {
        return submissionPath;
    }

    public void setSubmissionPath(String submissionPath) {
        this.submissionPath = submissionPath;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public List<String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(List<String> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public JudgeDTO getJudgeConfig() {
        return judgeConfig;
    }

    public void setJudgeConfig(JudgeDTO judgeConfig) {
        this.judgeConfig = judgeConfig;
    }

    public Runtime getRunner() {
        return runner;
    }

    public void setRunner(Runtime runner) {
        this.runner = runner;
    }

    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getResolutionPath() {
        return resolutionPath;
    }

    public void setResolutionPath(String resolutionPath) {
        this.resolutionPath = resolutionPath;
    }

    @Override
    public String toString() {
        return "JudgeConfigurationBO{" +
                "submissionPath='" + submissionPath + '\'' +
                ", submissionId='" + submissionId + '\'' +
                ", extraInfo=" + extraInfo +
                ", judgeConfig=" + judgeConfig +
                ", runner=" + runner +
                ", workPath='" + workPath + '\'' +
                ", scriptPath='" + scriptPath + '\'' +
                ", resolutionPath='" + resolutionPath + '\'' +
                '}';
    }
}
