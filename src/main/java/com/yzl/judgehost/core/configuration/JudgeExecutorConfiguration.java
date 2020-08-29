package com.yzl.judgehost.core.configuration;

import com.yzl.judgehost.core.factory.JudgeThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * 判题线程池
 *
 * <p>
 * 判题主要是cpu密集型的操作
 * <p>
 * 单核CPU上运行的多线程程序, 同一时间只能一个线程在跑
 * 系统帮你切换线程而已, 系统给每个线程分配时间片来执行
 * 每个时间片大概10ms左右, 看起来像是同时跑,
 * 但实际上是每个线程跑一点点就换到其它线程继续跑，
 * 多核并行量超过核心数目也有类似的道理
 * <p>
 * 所以, MAXIMUM_POOL_SIZE 的值改变影响不大，
 * 但是，由于我们要计算判题时间，所以必须等于cpu核心数目
 * 否则这个值会出错（例如单核双并行，值就相差了两倍）
 * <p>
 * KEEP_ALIVE_TIME           当线程数大于核心时，这是多余的空闲线程在终止之前等待新任务的最长时间。
 * BLOCKING_QUEUE_CAPACITY   允许排队的用户（请求）数量
 * 这两个值根据用户容忍的等待时间以及测试时单机任务执行平均时长来获取自定义的判题线程池的相关配置
 *
 * @author yuzhanglong
 * @date 2020-7-8 21:58
 */

@Configuration
@EnableAsync
public class JudgeExecutorConfiguration {
    public static final long KEEP_ALIVE_TIME = 5L;
    public static final String THREAD_NAME_PREFIX = "judge";
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final int BLOCKING_QUEUE_CAPACITY = 20;

    @Bean
    public ThreadPoolExecutor judgeHostServiceExecutor() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                new JudgeThreadFactory(THREAD_NAME_PREFIX),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
