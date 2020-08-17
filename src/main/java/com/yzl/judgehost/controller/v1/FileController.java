package com.yzl.judgehost.controller.v1;

import com.yzl.judgehost.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yuzhanglong
 * @date 2020-7-1 10:13
 * @description 判题相关文件处理相关接口
 */

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * @param response     HttpServletResponse
     * @param submissionId 某次提交的id
     * @author yuzhanglong
     * @description 下载某次提交的相关信息
     * @date 2020-8-17 20:48:17
     */
    @GetMapping("/download_submission/{submissionId}")
    public void downloadSubmissionById(@PathVariable String submissionId, HttpServletResponse response) {
        String zippedPath = fileService.getSubmissionDataById(submissionId);
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + submissionId + ".zip");
        fileService.writeOutputStream(zippedPath, response);
    }
}
