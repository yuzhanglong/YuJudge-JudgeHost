package com.yzl.judgehost.service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.yzl.judgehost.bo.JudgeHostConfigurationBO;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yuzhanglong
 * @description 一般功能性业务逻辑
 * @date 2020-7-30 20:03
 */

@Service
public class CommonService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;

    @Value("${server.port}")
    private Integer port;


    public CommonService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
    }

    public JudgeHostConfigurationBO getJudgeHostBasicInfo(){
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        JudgeHostConfigurationBO configuration = mapper.map(judgeEnvironmentConfiguration, JudgeHostConfigurationBO.class);
        configuration.setPort(port);
        return configuration;
    }
}
