package com.yzl.judgehost.core.configuration;

import com.yzl.judgehost.utils.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author yuzhanglong
 * @description 判题环境配置类
 */


@ConfigurationProperties(prefix = "judge-environment")
@Component
@Configuration
@PropertySource(value = "classpath:config/judge-environment.yml", factory = YamlPropertySourceFactory.class)
public class JudgeEnvironmentConfiguration {
    private String workPath;
    private String scriptPath;

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }


    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    @Override
    public String toString() {
        return "JudgeEnvironmentConfiguration{" +
                "path='" + workPath + '\'' +
                '}';
    }
}
