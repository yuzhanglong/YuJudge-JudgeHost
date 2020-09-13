package com.yzl.judgehost.store.redis;

import com.yzl.judgehost.utils.RedisOperations;
import com.yzl.judgehost.utils.TypeUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 判题相关缓存
 *
 * @author yuzhanglong
 * @date 2020-9-3 20:41:52
 */

@Component
public class JudgeCache {
    public static final String SOLUTION_REMOTE_URL_TO_LOCAL_MAP_REDIS_SAVE_PREFIX = "remote_solution_to_local_paths";
    private final RedisOperations redisOperations;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public JudgeCache(RedisOperations redisOperations) {
        this.redisOperations = redisOperations;
    }

    /**
     * 添加远程文件地址 -- 本地文件的映射关系
     *
     * @param remotePath 远程目录
     * @param localPaths 本地目录
     * @author yuzhanglong
     * @date 2020-9-3 20:41:52
     */
    public void addSolutionRemoteUrlToLocalMap(String remotePath, Map<String, String> localPaths) {
        // 写数据时我们不允许读，更不允许写，因此我们使用读写锁
        readWriteLock.writeLock().lock();
        boolean isSet = redisOperations.setHashMap(SOLUTION_REMOTE_URL_TO_LOCAL_MAP_REDIS_SAVE_PREFIX, remotePath, localPaths);
        readWriteLock.writeLock().unlock();
    }

    /**
     * 获取解决方案本地目录
     *
     * @param remotePath 远程目录
     * @author yuzhanglong
     * @date 2020-9-3 20:59:43
     */
    public Map<String, String> getSolutionLocalPathByRemoteUrl(String remotePath) {
        readWriteLock.readLock().lock();
        Object res = redisOperations.getHashMapByItemKey(SOLUTION_REMOTE_URL_TO_LOCAL_MAP_REDIS_SAVE_PREFIX, remotePath);
        readWriteLock.readLock().unlock();
        return TypeUtil.castObjectToHashMap(res, String.class, String.class);
    }

    /**
     * 移除解决方案映射表的某个内容
     *
     * @param itemKey 内容key
     * @author yuzhanglong
     * @date 2020-9-3 21:58:30
     */
    public void removeRemotePathToLocalPathMapItem(String itemKey) {
        readWriteLock.writeLock().lock();
        redisOperations.deleteHashMapItem(SOLUTION_REMOTE_URL_TO_LOCAL_MAP_REDIS_SAVE_PREFIX, itemKey);
        readWriteLock.writeLock().unlock();
    }
}
