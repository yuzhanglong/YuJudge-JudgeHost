package com.yzl.judgehost.utils;

import com.yzl.judgehost.exception.http.NotFoundException;

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

    /**
     * 清空某个文件夹下的所有文件
     *
     * @param filePath 需要删除的文件目录
     * @return 文件是否删除成功
     * @author yuzhanglong
     * @date 2020-09-03 18:22:09
     */
    public static Boolean clearFileByFolderName(String filePath) {
        File file = new File(filePath);
        return deleteFile(file, false);
    }

    /**
     * 删除某个文件
     *
     * @param file       文件对象
     * @param deleteSelf 是否删除自身
     * @return 文件是否删除成功
     * @author yuzhanglong
     * @date 2020-9-3 11:06:25
     */
    public static Boolean deleteFile(File file, Boolean deleteSelf) {
        //判断文件不为null或文件目录存在
        if (!file.exists()) {
            throw new NotFoundException("B1007");
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        if (files != null) {
            for (File f : files) {
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()) {
                    deleteFile(f, true);
                } else {
                    boolean res = f.delete();
                }
            }
        }
        if (deleteSelf) {
            return file.delete();
        }
        return true;
    }

    /**
     * 将内容写文件到某个路径下
     *
     * @param data 文本内容
     * @param file 文件(路径)对象
     * @return 是否写入成功
     * @author yuzhanglong
     * @date 2020-9-8 01:00:22
     */
    public static Boolean writeDataToFilePath(String data, File file) {
        // 如果文件不存在，创建之
        if (!file.exists()) {
            try {
                boolean isSuccess = file.createNewFile();
                if (!isSuccess) {
                    return false;
                }
                FileWriter writer = new FileWriter(file.getName(), true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(data);
                bufferWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
