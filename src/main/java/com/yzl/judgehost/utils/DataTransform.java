package com.yzl.judgehost.utils;

import java.util.List;

/**
 * 业务中的数据类型转换相关
 *
 * @author yuzhanglong
 * @date 2020-8-17 20:54:57
 */
public class DataTransform {
    public static String stringListToString(List<String> stringList) {
        StringBuilder result = new StringBuilder();
        stringList.forEach(result::append);
        System.out.println("=" + result);
        return result.toString();
    }
}
