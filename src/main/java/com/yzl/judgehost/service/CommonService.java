package com.yzl.judgehost.service;

import com.github.dozermapper.core.Mapper;
import com.sun.management.OperatingSystemMXBean;
import com.yzl.judgehost.bo.JudgeHostConditionBO;
import com.yzl.judgehost.bo.JudgeHostConfigurationBO;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.configuration.JudgeExecutorConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;

/**
 * @author yuzhanglong
 * @description 一般功能性业务逻辑
 * @date 2020-7-30 20:03
 */

@Service
public class CommonService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    private final Mapper mapper;
    private final JudgeExecutorConfiguration judgeExecutorConfiguration;

    @Value("${server.port}")
    private Integer port;


    public CommonService(
            JudgeEnvironmentConfiguration judgeEnvironmentConfiguration,
            Mapper mapper,
            JudgeExecutorConfiguration judgeExecutorConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
        this.mapper = mapper;
        this.judgeExecutorConfiguration = judgeExecutorConfiguration;
    }

    /**
     * @author yuzhanglong
     * @description 获取判题机状态
     * @date 2020-8-17 19:25:11
     */
    public JudgeHostConfigurationBO getJudgeHostConfiguration() {
        JudgeHostConfigurationBO configuration = mapper.map(judgeEnvironmentConfiguration, JudgeHostConfigurationBO.class);
        configuration.setPort(port);
        return configuration;
    }

    /**
     * @author yuzhanglong
     * @description 获取判题机线程池状态
     * @date 2020-8-17 19:46:55
     */
    public JudgeHostConditionBO getJudgeHostCondition() {
        Integer cpuCoreAmount = JudgeExecutorConfiguration.MAXIMUM_POOL_SIZE - 1;
        Integer workingAmount = judgeExecutorConfiguration.judgeHostServiceExecutor().getActiveCount();
        Integer queueAmount = judgeExecutorConfiguration.judgeHostServiceExecutor().getQueue().size();
        Integer cpu = getCpuCostPercentage();
        Integer memory = getMemoryCostPercentage();
        return new JudgeHostConditionBO(workingAmount, cpuCoreAmount, memory, cpu, queueAmount);
    }

    /**
     * @author yuzhanglong
     * @description 获取cpu占用率
     * @date 2020-8-17 20:05:18
     */
    private Integer getCpuCostPercentage() {
        OperatingSystemMXBean system = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = system.getSystemCpuLoad();
        return (int) (cpuLoad * 100);
    }

    /**
     * @author yuzhanglong
     * @description 获取系统内存占用率
     * @date 2020-8-17 20:05:18
     */
    private Integer getMemoryCostPercentage() {
        OperatingSystemMXBean system = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double totalVirtualMemory = system.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = system.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize / totalVirtualMemory;
        return (int) ((1 - value) * 100);
    }
}
