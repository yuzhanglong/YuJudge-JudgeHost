package com.yzl.judgehost.core.configuration;

import com.yzl.judgehost.core.common.JudgeThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author yuzhanglong
 * @description 自定义的判题线程池
 * 判题虽然有一定的io操作，
 * 但是主要还是cpu密集型的操作，
 * 经并发检测执行时cpu的占用几乎都在百分之百
 * <p>
 * 其中：
 * KEEP_ALIVE_TIME 当线程数大于核心时，这是多余的空闲线程在终止之前等待新任务的最长时间。
 * CORE_POOL_SIZE 保留在池中的线​​程数，即使处于空闲状态
 * BLOCKING_QUEUE_CAPACITY 允许排队的用户（请求）数量
 * 这些值应该根据用户容忍的等待时间以及测试时单机任务执行平均时长来获取自定义的判题线程池的相关配置
 * @date 2020-7-8 21:58
 */

@Configuration
@EnableAsync
public class JudgeExecutorConfiguration {
    final int CORE_POOL_SIZE = 1;
    final long KEEP_ALIVE_TIME = 5L;
    final int BLOCKING_QUEUE_CAPACITY = 4;
    final String THREAD_NAME_PREFIX = "judger";

    @Bean
    public Executor asyncServiceExecutor() {
        // cpu密集型 池中允许的最大线程数 在判题这样的CPU密集型任务中，我们应该将其设置为 cpu核心数 + 1
        final int maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                maximumPoolSize,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                new JudgeThreadFactory(THREAD_NAME_PREFIX),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return executor;
    }
}
