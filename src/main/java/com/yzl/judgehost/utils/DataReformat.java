package com.yzl.judgehost.utils;

import java.util.List;

/**
 * @author yuzhanglong
 * @description 业务中的数据类型转换相关
 */
public class DataReformat {
    public static String stringListToString(List<String> stringList) {
        StringBuilder result = new StringBuilder();
        stringList.forEach(result::append);
        return result.toString();
    }
}
