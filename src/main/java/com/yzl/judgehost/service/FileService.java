package com.yzl.judgehost.service;

import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.exception.http.NotFoundException;
import com.yzl.judgehost.utils.FileHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * @author yuzhanglong
 * @description 文件处理相关业务
 * @date 2020-7-1 12:33
 */
@Service
public class FileService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;

    public FileService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
    }

    /**
     * @param submissionId 某次提交的id
     * @author yuzhanglong
     * @description 读取某次提交的文件夹，将内容压缩过之后返回
     * @date 2020-7-1 17:32
     */
    public String getSubmissionDataById(String submissionId) {
        String submissionPath = getSubmissionPathById(submissionId);
        // 压缩目标文件
        return zipSubmissionFolder(submissionPath);
    }

    /**
     * @param submissionId 某次提交的id
     * @author yuzhanglong
     * @description 获取某次提交的工作目录
     * @date 2020-7-1 17:32
     */
    private String getSubmissionPathById(String submissionId) {
        return this.judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + submissionId;
    }

    /**
     * @param submissionPath 提交路径
     * @author yuzhanglong
     * @description 返回压缩后的压缩包目录
     * @date 2020-7-1 18:11
     */
    private String zipSubmissionFolder(String submissionPath) {
        if (!FileHelper.isDirectory(submissionPath)) {
            throw new NotFoundException("B1003");
        }
        String zippedPath = submissionPath + "/" + UUID.randomUUID() + ".zip";
        boolean isZipped = false;
        try {
            isZipped = FileHelper.zipDictionary(zippedPath, submissionPath);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        if (!isZipped) {
            throw new NotFoundException("B1003");
        }
        return zippedPath;
    }
}
