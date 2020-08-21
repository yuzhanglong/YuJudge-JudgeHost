package com.yzl.judgehost.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理相关工具类
 *
 * @author yuzhanglong
 * @date 2020-6-30 11:03:47
 */
public class FileUtil {
    /**
     * 判断某个目录下的文件是否存在
     *
     * @param filePath 需要判断的文件目录
     * @return Boolean 文件是否存在
     * @author yuzhanglong
     * @date 2020-6-30 11:03:47
     */
    public static Boolean isFileIn(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 判断某个工作目录是否存在
     *
     * @param filePath 需要判断的工作目录
     * @return Boolean 判断某个工作目录是否存在
     * @author yuzhanglong
     * @date 2020-6-30 11:03:47
     */
    public static Boolean isDirectory(String filePath) {
        File directory = new File(filePath);
        return directory.isDirectory();
    }

    /**
     * 压缩某个文件夹，并保存到目标位置
     *
     * @param zippedPath 压缩文件保存的目录
     * @param targetPath 被压缩的目录
     * @return Boolean 是否压缩成功
     * @throws IOException an I/O exception
     * @author yuzhanglong
     * @date 2020-6-30 11:03:47
     */
    public static Boolean zipDictionary(String zippedPath, String targetPath) throws InterruptedException, IOException {
        if (!isDirectory(targetPath)) {
            return false;
        } else {
            Runtime runtime = Runtime.getRuntime();
            String[] zipCommand = {
                    "zip",
                    "-j",
                    "-r",
                    zippedPath,
                    targetPath
            };
            Process process = runtime.exec(zipCommand);
            process.waitFor();
        }
        return true;
    }


    /**
     * 判断某个工作目录是否存在
     *
     * @param filePath 需要读取的文件目录
     * @return 文件内容，以行代表数组的下标
     * @throws IOException an I/O exception
     * @author yuzhanglong
     * @date 2020-6-30 11:03:47
     */
    public static List<String> readFileByLines(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String tempStr;
        List<String> stringList = new ArrayList<>();
        while ((tempStr = bufferedReader.readLine()) != null) {
            stringList.add(tempStr);
        }
        return stringList;
    }
}
