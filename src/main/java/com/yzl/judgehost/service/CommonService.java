package com.yzl.judgehost.service;

import com.github.dozermapper.core.Mapper;
import com.sun.management.OperatingSystemMXBean;
import com.yzl.judgehost.bo.JudgeHostConditionBO;
import com.yzl.judgehost.bo.JudgeHostConfigurationBO;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.configuration.JudgeExecutorConfiguration;
import com.yzl.judgehost.exception.http.ForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;

/**
 * 一般功能性业务逻辑
 *
 * @author yuzhanglong
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
     * 获取判题机状态
     *
     * @author yuzhanglong
     * @date 2020-8-17 19:25:11
     */
    public JudgeHostConfigurationBO getJudgeHostConfiguration() {
        JudgeHostConfigurationBO configuration = mapper.map(judgeEnvironmentConfiguration, JudgeHostConfigurationBO.class);
        configuration.setPort(port);
        return configuration;
    }

    /**
     * 获取判题机线程池状态
     *
     * @author yuzhanglong
     * @date 2020-8-17 19:46:55
     */
    public JudgeHostConditionBO getJudgeHostCondition() {
        Integer cpuCoreAmount = Runtime.getRuntime().availableProcessors();
        Integer workingAmount = judgeExecutorConfiguration.judgeHostServiceExecutor().getActiveCount();
        Integer queueAmount = judgeExecutorConfiguration.judgeHostServiceExecutor().getQueue().size();
        Integer cpu = getCpuCostPercentage();
        Integer memory = getMemoryCostPercentage();
        Integer maxWorkingAmount = judgeExecutorConfiguration.judgeHostServiceExecutor().getMaximumPoolSize();
        return new JudgeHostConditionBO(workingAmount, cpuCoreAmount, memory, cpu, queueAmount, maxWorkingAmount);
    }

    /**
     * 获取cpu占用率
     *
     * @author yuzhanglong
     * @date 2020-8-17 20:05:18
     */
    private Integer getCpuCostPercentage() {
        OperatingSystemMXBean system = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = system.getSystemCpuLoad();
        return (int) (cpuLoad * 100);
    }

    /**
     * 获取系统内存占用率
     *
     * @author yuzhanglong
     * @date 2020-8-17 20:05:18
     */
    private Integer getMemoryCostPercentage() {
        OperatingSystemMXBean system = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double totalVirtualMemory = system.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = system.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize / totalVirtualMemory;
        return (int) ((1 - value) * 100);
    }

    /**
     * 设置判题节点数量
     *
     * @author yuzhanglong
     * @date 2020-8-29 17:14:25
     */
    public void setJudgeHostWorkingAmount(Integer corePoolSize, Boolean isForceSet) {
        //TODO: 这里需要对设置的值作出范围类的限制，防止运行节点过多导致崩溃
        if (isForceSet == null) {
            isForceSet = false;
        }
        boolean isHavingWorkingNode = judgeExecutorConfiguration.judgeHostServiceExecutor().getActiveCount() != 0;
        if (!isForceSet && isHavingWorkingNode) {
            // 如果不是强制修改，且有任务进行中，则拒绝修改
            throw new ForbiddenException("B1006");
        }
        judgeExecutorConfiguration.judgeHostServiceExecutor().setCorePoolSize(corePoolSize);
        judgeExecutorConfiguration.judgeHostServiceExecutor().setMaximumPoolSize(corePoolSize);
    }
}