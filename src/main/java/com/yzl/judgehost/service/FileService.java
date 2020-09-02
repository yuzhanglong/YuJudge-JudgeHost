package com.yzl.judgehost.service;

import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.exception.http.NotFoundException;
import com.yzl.judgehost.utils.FileUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件处理相关业务
 *
 * @author yuzhanglong
 * @date 2020-7-1 12:33
 */
@Service
public class FileService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;

    public FileService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
    }

    /**
     * 读取某次提交的文件夹，将内容压缩过之后返回
     *
     * @param submissionId 某次提交的id
     * @author yuzhanglong
     * @date 2020-7-1 17:32
     */
    public String getSubmissionDataById(String submissionId) {
        String submissionPath = getSubmissionPathById(submissionId);
        // 压缩目标文件
        return zipSubmissionFolder(submissionPath);
    }

    /**
     * 获取某次提交的工作目录
     *
     * @param submissionId 某次提交的id
     * @author yuzhanglong
     * @date 2020-7-1 17:32
     */
    private String getSubmissionPathById(String submissionId) {
        return this.judgeEnvironmentConfiguration.getSubmissionPath() + "/" + submissionId;
    }

    /**
     * 返回压缩后的压缩包目录
     *
     * @param submissionPath 提交路径
     * @author yuzhanglong
     * @date 2020-7-1 18:11
     */
    private String zipSubmissionFolder(String submissionPath) {
        if (!FileUtil.isDirectory(submissionPath)) {
            throw new NotFoundException("B1003");
        }
        String zippedPath = submissionPath + "/" + UUID.randomUUID() + ".zip";
        boolean isZipped = false;
        try {
            isZipped = FileUtil.zipDictionary(zippedPath, submissionPath);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        if (!isZipped) {
            throw new NotFoundException("B1003");
        }
        return zippedPath;
    }

    /**
     * 文件转化输出流
     *
     * @param filePath 文件路径
     * @author yuzhanglong
     * @date 2020-8-17 20:51:33
     */
    public void writeOutputStream(String filePath, HttpServletResponse response) {
        File file = new File(filePath);
        byte[] buffer = new byte[1024];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            OutputStream outputStream = response.getOutputStream();
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, i);
                i = bufferedInputStream.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
