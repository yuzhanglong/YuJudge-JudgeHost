package com.yzl.judgehost.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作集类
 *
 * @author yuzhanglong
 * @date 2020-08-03 20:44:55
 */

@Component
public class RedisOperations {
    private final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;


    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisOperations(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置key-value对
     *
     * @param key   键
     * @param value 值
     * @author yuzhanglong
     * @date 2020-08-03 20:49:41
     */

    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取key对应的value
     *
     * @param key 键
     * @author yuzhanglong
     * @date 2020-08-03 21:18:38
     */
    public Object get(String key) {
        return key != null ? redisTemplate.opsForValue().get(key) : null;
    }


    /**
     * 设置key-value对
     *
     * @param key  键
     * @param time 时间量
     * @author yuzhanglong
     * @date 2020-08-03 20:49:41
     */

    public Boolean expireKey(String key, Long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, DEFAULT_TIME_UNIT);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取某个key的过期时间
     *
     * @param key 需要获取过期时间的key
     * @author yuzhanglong
     * @date 2020-08-03 20:58:44
     */

    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key, DEFAULT_TIME_UNIT);
    }


    /**
     * 设置key-value对，同时为其设置了时间限制
     *
     * @author yuzhanglong
     * @date 2020-08-03 21:03:07
     */
    public Boolean set(String key, Object value, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, DEFAULT_TIME_UNIT);
            } else {
                boolean isSet = set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个或者多个key
     *
     * @param keys 需要删除的key(s)
     * @author yuzhanglong
     * @date 2020-08-03 22:15:05
     */
    public void remove(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String s : keys) {
                redisTemplate.delete(s);
            }
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0到-1代表所有值
     * @return List<Object> list内容
     */
    public List<Object> getList(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return Boolean 是否成功
     */
    public Boolean setList(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向hash表中放入数据, 如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public Boolean setHashMap(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void deleteHashMapItem(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 获取hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object getHashMapByItemKey(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> getHashMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}