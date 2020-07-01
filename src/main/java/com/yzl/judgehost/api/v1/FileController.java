package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yuzhanglong
 * @date  2020-7-1 10:13
 * @description  判题相关文件处理相关接口
 */

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download_submission/{submissionId}")
    public void downloadSubmissionById(@PathVariable String submissionId, HttpServletResponse response) {
        String zippedPath = fileService.getSubmissionDataById(submissionId);
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + submissionId + ".zip");
        File file = new File(zippedPath);
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
