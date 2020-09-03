package com.yzl.judgehost.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型处理工具类
 *
 * @author yuzhanglong
 * @date 2020-9-3 21:36:17
 */
public class TypeUtil {
    /**
     * object转Map
     *
     * @author yuzhanglong
     * @date 2020-9-3 21:44:32
     */
    public static <K, V> Map<K, V> castObjectToHashMap(Object obj, Class<K> classKey, Class<V> classValue) {
        Map<K, V> result = new HashMap<>(5);
        if (obj instanceof Map<?, ?>) {
            ((Map<?, ?>) obj).forEach((k, v) -> {
                result.put(classKey.cast(k), classValue.cast(v));
            });
            return result;
        }
        return null;
    }
}
