package com.yzl.judgehost.bo;

/**
 * 判题服务器配置bo对象
 *
 * @author yuzhanglong
 * @date 2020-7-30 20:15
 */
public class JudgeHostConfigurationBO {
    private String workPath;
    private String scriptPath;
    private String resolutionPath;
    private Integer port;

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "JudgeHostConfigurationBO{" +
                "workPath='" + workPath + '\'' +
                ", scriptPath='" + scriptPath + '\'' +
                ", resolutionPath='" + resolutionPath + '\'' +
                ", port=" + port +
                '}';
    }
}
