package com.yzl.judgehost.core.configuration;

import com.yzl.judgehost.exception.http.NotFoundException;
import com.yzl.judgehost.utils.FileUtil;
import com.yzl.judgehost.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 判题环境配置类
 *
 * @author yuzhanglong
 * @date 2020-6-30 11:24:04
 */

@ConfigurationProperties(prefix = "judge-environment")
@Component
@Configuration
@PropertySource(value = "classpath:config/judge-environment.yml", factory = YamlPropertySourceFactory.class)
public class JudgeEnvironmentConfiguration {
    private String submissionPath;
    private String scriptPath;
    private String resolutionPath;

    /**
     * 判断相关文件依赖是否存在
     *
     * @throws RuntimeException 项目依赖的文件、文件夹路径不存在
     * @author yuzhanglong
     * @date 2020-6-30 11:24:04
     */
    public void checkJudgeEnvironmentBaseFileIn() {
        // TODO:将它们移到springboot启动类中
        if (!FileUtil.isDirectory(resolutionPath)) {
            throw new NotFoundException("B1002");
        }
        if (!FileUtil.isDirectory(submissionPath)) {
            throw new NotFoundException("B1002");
        }
        if (!FileUtil.isDirectory(scriptPath)) {
            throw new NotFoundException("B1002");
        }
    }

    public String getSubmissionPath() {
        return submissionPath;
    }

    public void setSubmissionPath(String submissionPath) {
        this.submissionPath = submissionPath;
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
        return "JudgeEnvironmentConfiguration{" +
                "submissionPath='" + submissionPath + '\'' +
                ", scriptPath='" + scriptPath + '\'' +
                ", resolutionPath='" + resolutionPath + '\'' +
                '}';
    }
}