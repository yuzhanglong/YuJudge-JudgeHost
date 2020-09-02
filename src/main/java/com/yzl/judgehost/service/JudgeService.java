package com.yzl.judgehost.service;

import com.alibaba.fastjson.JSON;
import com.yzl.judgehost.bo.JudgeConfigurationBO;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.configuration.JudgeExecutorConfiguration;
import com.yzl.judgehost.core.enumeration.JudgeResultEnum;
import com.yzl.judgehost.core.enumeration.LanguageScriptEnum;
import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.dto.SolutionDTO;
import com.yzl.judgehost.exception.http.NotFoundException;
import com.yzl.judgehost.network.HttpRequest;
import com.yzl.judgehost.dto.SingleJudgeResultDTO;
import com.yzl.judgehost.utils.DataTransform;
import com.yzl.judgehost.utils.FileUtil;
import com.yzl.judgehost.utils.JudgeHolder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 判题服务模块
 *
 * @author yuzhanglong
 * @date 2020-6-24 12:10:43
 */

@Service
public class JudgeService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;


    public JudgeService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
    }

    /**
     * 调用compile.sh 生成脚本
     *
     * @return String 编译返回的信息，如果没有信息，则编译成功
     * @author yuzhanglong
     * @date 2020-6-24 12:10:43
     */
    private List<String> compileSubmission() {
        // 编译脚本
        String compileScript = JudgeHolder.getCompileScriptPath();

        // 本次提交工作目录
        String submissionWorkingPath = JudgeHolder.getSubmissionWorkingPath();

        // 判题核心脚本
        String judgeCoreScript = JudgeHolder.getJudgeCoreScriptPath();
        if (!FileUtil.isFileIn(compileScript)) {
            throw new NotFoundException("B1002");
        }

        JudgeDTO judgeDTO = JudgeHolder.getJudgeConfig();
        // 获取编程语言
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeDTO.getLanguage());

        // 用户代码
        String codePath = JudgeHolder.getCodePath(language.getExtensionName());

        // 对应语言的编译脚本
        String buildScript = language.getBuildScriptByRunningPath(submissionWorkingPath, codePath);

        List<String> result = new ArrayList<>();
        try {
            Process process = JudgeHolder.getRunner().exec(
                    new String[]{
                            compileScript,
                            submissionWorkingPath,
                            codePath,
                            judgeDTO.getSubmissionCode(),
                            buildScript,
                            judgeCoreScript
                    });
            process.waitFor();
            result = readStdout(process);
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    /**
     * 执行判题
     *
     * @param judgeDTO judgeDTO对象
     * @return CompletableFuture<List < SingleJudgeResultDTO>>由一个或多个单次判题结果组成的list，以CompletableFuture包装
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     */
    @SuppressWarnings("DuplicatedCode")
    @Async(value = "judgeHostServiceExecutor")
    public CompletableFuture<List<SingleJudgeResultDTO>> runJudge(JudgeDTO judgeDTO) {
        JudgeConfigurationBO judgeConfigurationBO = new JudgeConfigurationBO(
                judgeDTO,
                judgeEnvironmentConfiguration.getSubmissionPath(),
                judgeEnvironmentConfiguration.getScriptPath(),
                judgeEnvironmentConfiguration.getResolutionPath()
        );
        JudgeHolder.initJudgeConfiguration(judgeConfigurationBO);


        // 编译用户的提交
        List<String> compileResult = compileSubmission();
        List<SingleJudgeResultDTO> result = new ArrayList<>();
//        // 编译阶段成功，开始运行用户代码
//        if (isCompileSuccess(compileResult)) {
//            List<SolutionDTO> totalResolution = judgeDTO.getSolutions();
//            for (SolutionDTO solutionDTO : totalResolution) {
//                SingleJudgeResultDTO singleJudgeResult = compileResult();
//                boolean isAccept = singleJudgeResult.getCondition().equals(JudgeResultEnum.ACCEPTED.getNumber());
//                // 这个测试点没有通过，并且是acm模式
//                result.add(singleJudgeResult);
//                if (!isAccept && judgeDTO.isAcmMode()) {
//                    break;
//                }
//                // oi模式，继续执行判题
//            }
//        } else {
//            SingleJudgeResultDTO resolution = new SingleJudgeResultDTO();
//            resolution.setCondition(JudgeResultEnum.COMPILE_ERROR.getNumber());
//            resolution.setMessageWithCondition();
//            result.add(resolution);
//        }
        return CompletableFuture.completedFuture(result);
    }


    /**
     * 获取运行的脚本/可执行文件的输出
     *
     * @param process 运行的进程对象
     * @return String 进程输出
     * @throws IOException an I/O exception
     * @author yuzhanglong
     * @date 2020-6-30 21:21
     */
    private List<String> readStdout(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> stringList = new ArrayList<>();
        String temp;
        while ((temp = reader.readLine()) != null) {
            stringList.add(temp);
        }
        BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((temp = errReader.readLine()) != null) {
            stringList.add(temp);
        }
        // TODO:处理错误输出
        return stringList;
    }

    /**
     * 传入编译结果，根据语言特性来判断编译是否成功
     *
     * @param compileResult 编译结果
     * @return Boolean 编译是否成功
     * @author yuzhanglong
     * @date 2020-6-30 21:21
     */
    private Boolean isCompileSuccess(List<String> compileResult) {
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(JudgeHolder.getJudgeConfig().getLanguage());
        // c语言家族（c && cpp）
        boolean isCppFamily = (language == LanguageScriptEnum.C || language == LanguageScriptEnum.C_PLUS_PLUS);
        // java
        boolean isJava = (language == LanguageScriptEnum.JAVA);
        // 另外，python 属于解释性语言，不在此处考虑
        for (String str : compileResult) {
            boolean isBad = str.contains("error:") || str.contains("错误：") || str.contains("Error:");
            if (isCppFamily && isBad) {
                return false;
            }
            if (isJava && isBad) {
                return false;
            }
        }
        return true;
    }

    /**
     * 编译错误会返回错误，类似的，我们在运行判题核心时也可能产生错误
     * 例如：python（解释性语言）的运行错误提示
     *
     * @return List<String> 错误内容，我们用数组存储，用下标来代表行
     * @author yuzhanglong
     * @date 2020-7-1 9:22
     */
    private List<String> getJudgeCoreStderr(String stderrPath) {
        List<String> judgeErrors = null;
        try {
            judgeErrors = FileUtil.readFileByLines(stderrPath);
        } catch (IOException ioException) {
            //TODO:找不到stderr的错误处理
            ioException.printStackTrace();
        }
        return judgeErrors;
    }
}