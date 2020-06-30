package com.yzl.judgehost.utils;

import java.io.File;

/**
 * @author yuzhanglong
 * @description 文件处理相关工具类
 * @date 2020-6-30 11:03:47
 */
public class FileHelper {
    /**
     * @param filePath 需要判断的文件目录
     * @author yuzhanglong
     * @description 判断某个目录下的文件是否存在
     * @return Boolean 文件是否存在
     * @date 2020-6-30 11:03:47
     */
    public static Boolean isFileIn(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    public static Boolean isDirectory(String filePath){
        File directory = new File(filePath);
        return directory.isDirectory();
    }
}
