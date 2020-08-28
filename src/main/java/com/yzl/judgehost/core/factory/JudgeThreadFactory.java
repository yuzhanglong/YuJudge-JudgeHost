package com.yzl.judgehost.core.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义判题线程工厂
 *
 * @author yuzhanglong
 * @date 2020-7-6 22:33
 */
public class JudgeThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public JudgeThreadFactory(String name) {
        namePrefix = "From JudgeThreadFactory's " + name + "-Worker-";
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        return new Thread(null, task, name, 0);
    }


}
