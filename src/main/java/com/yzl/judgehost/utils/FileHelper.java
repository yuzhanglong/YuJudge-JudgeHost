package com.yzl.judgehost.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuzhanglong
 * @description 文件处理相关工具类
 * @date 2020-6-30 11:03:47
 */
public class FileHelper {
    /**
     * @param filePath 需要判断的文件目录
     * @return Boolean 文件是否存在
     * @author yuzhanglong
     * @description 判断某个目录下的文件是否存在
     * @date 2020-6-30 11:03:47
     */
    public static Boolean isFileIn(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * @param filePath 需要判断的工作目录
     * @return Boolean 判断某个工作目录是否存在
     * @author yuzhanglong
     * @description 判断某个工作目录是否存在
     * @date 2020-6-30 11:03:47
     */
    public static Boolean isDirectory(String filePath) {
        File directory = new File(filePath);
        return directory.isDirectory();
    }


    /**
     * @param filePath 需要读取的文件目录
     * @return List<String>  文件内容，以行代表数组的下标
     * @throws IOException an I/O exception
     * @author yuzhanglong
     * @description 判断某个工作目录是否存在
     * @date 2020-6-30 11:03:47
     */
    public static List<String> readFileByLines(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new FileReader(file));
        String tempStr;
        List<String> stringList = new ArrayList<>();
        while ((tempStr = bufferedReader.readLine()) != null) {
            stringList.add(tempStr);
        }
        return stringList;
    }
}
