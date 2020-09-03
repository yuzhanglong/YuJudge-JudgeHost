package com.yzl.judgehost.service;

import com.alibaba.fastjson.JSON;
import com.yzl.judgehost.bo.JudgeConfigurationBO;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
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
import com.yzl.judgehost.vo.JudgeConditionVO;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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
    public static final String SOLUTION_STD_IN_PATH_KEY = "stdIn";
    public static final String SOLUTION_EXPECTED_STD_OUT_PATH_KEY = "expectedStdOut";
    public static final int USE_JUDGE_CORE_GUARD = 1;

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

        // 执行编译脚本
        try {
            Process process = JudgeHolder.getRunner().exec(
                    new String[]{
                            compileScript,
                            submissionWorkingPath,
                            codePath,
                            judgeDTO.getSubmissionCode(),
                            buildScript,
                            judgeCoreScript,
                            language == LanguageScriptEnum.JAVA ? "0" : "2000"
                    });
            process.waitFor();
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        return readFile(submissionWorkingPath + "/" + JudgeHolder.COMPILE_STD_ERR_NAME);
    }

    /**
     * 执行判题
     *
     * @param judgeDTO judgeDTO对象
     * @return CompletableFuture<List < SingleJudgeResultDTO>>由一个或多个单次判题结果组成的list，以CompletableFuture包装
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     */
    @Async(value = "judgeHostServiceExecutor")
    public CompletableFuture<JudgeConditionVO> runJudge(JudgeDTO judgeDTO) {

        JudgeConfigurationBO judgeConfigurationBO = new JudgeConfigurationBO(
                judgeDTO,
                judgeEnvironmentConfiguration.getSubmissionPath(),
                judgeEnvironmentConfiguration.getScriptPath(),
                judgeEnvironmentConfiguration.getResolutionPath()
        );
        JudgeHolder.initJudgeConfiguration(judgeConfigurationBO);

        // 编译用户的提交
        List<String> compileResult = compileSubmission();
        JudgeHolder.setExtraInfo(compileResult);
        List<SingleJudgeResultDTO> result = new ArrayList<>();
        // 编译阶段成功，开始运行用户代码
        if (isCompileSuccess(compileResult)) {
            List<SolutionDTO> totalResolution = judgeDTO.getSolutions();
            int solutionIndex = 0;
            for (SolutionDTO solutionDTO : totalResolution) {
                SingleJudgeResultDTO singleJudgeResult = runForSingleJudge(solutionDTO, solutionIndex);
                boolean isAccept = singleJudgeResult.getCondition().equals(JudgeResultEnum.ACCEPTED.getNumber());
                // 这个测试点没有通过，并且是acm模式
                result.add(singleJudgeResult);
                if (!isAccept && judgeDTO.isAcmMode()) {
                    break;
                }
                // oi模式，继续执行判题
                solutionIndex++;
            }
        } else {
            SingleJudgeResultDTO resolution = new SingleJudgeResultDTO();
            resolution.setCondition(JudgeResultEnum.COMPILE_ERROR.getNumber());
            resolution.setMessageWithCondition();
            result.add(resolution);
        }
        return CompletableFuture.completedFuture(new JudgeConditionVO(result, JudgeHolder.getExtraInfo(), JudgeHolder.getSubmissionId()));
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
     * 读取某个文件的内容
     *
     * @param filePath 文件路径
     * @return List<String> 错误内容，我们用数组存储，用下标来代表行
     * @author yuzhanglong
     * @date 2020-7-1 9:22
     */
    private List<String> readFile(String filePath) {
        List<String> judgeErrors = null;
        try {
            judgeErrors = FileUtil.readFileByLines(filePath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return judgeErrors;
    }

    /**
     * 根据期望数据来执行单次判题
     *
     * @param singleResolution 用户传入的单次判题的正确解决方案，参见ResolutionDTO类
     * @param index            第index个测试点
     * @return SingleJudgeResultDTO 单次判题结果
     * @author yuzhanglong
     * @date 2020-7-1 9:47
     * @see SolutionDTO
     */
    private SingleJudgeResultDTO runForSingleJudge(SolutionDTO singleResolution, Integer index) {
        String singleJudgeRunningName = "running_" + index.toString();
        Map<String, String> resolution = getResolutionInputAndOutputFile(singleResolution, "solution");
        SingleJudgeResultDTO singleJudgeResult = startJudging(resolution.get(SOLUTION_STD_IN_PATH_KEY), singleJudgeRunningName);
        List<String> judgeCoreStdErr = readFile(singleJudgeResult.getStdErrPath());

        // 没有stderr输出时
        if (judgeCoreStdErr.size() == 0) {
            Boolean isRunSuccess = singleJudgeResult.getCondition() == 1;
            // 对比
            Boolean isPass = compareOutputWithResolutions(singleJudgeResult.getStdOutPath(), resolution.get(SOLUTION_EXPECTED_STD_OUT_PATH_KEY));
            // 如果通过，将condition设置为 0
            if (isPass && isRunSuccess) {
                singleJudgeResult.setCondition(JudgeResultEnum.ACCEPTED.getNumber());
            }
        } else {
            JudgeHolder.setExtraInfo(judgeCoreStdErr);
            singleJudgeResult.setCondition(JudgeResultEnum.RUNTIME_ERROR.getNumber());
        }
        singleJudgeResult.setMessageWithCondition();
        return singleJudgeResult;
    }

    /**
     * 获取输入文件和期望的输出文件，供后续判题使用
     *
     * @param resolution 解决方案数据传输对象
     * @param name       输出文件名称
     * @return 保存了输入文件、输出文件本地地址的hashMap
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     */
    private Map<String, String> getResolutionInputAndOutputFile(SolutionDTO resolution, String name) {
        String inputFile = resolution.getStdIn();
        String outputFile = resolution.getExpectedStdOut();

        // 下载、获取输入和期望输出
        // TODO: 使用redis缓存url到本地路径的映射, 减少请求次数
        Resource inputFileResource = HttpRequest.getFile(inputFile);
        Resource outputFileResource = HttpRequest.getFile(outputFile);
        UUID p = UUID.randomUUID();
        String inPath = JudgeHolder.getResolutionPath() + "/" + p + "/" + name + ".in";
        String outPath = JudgeHolder.getResolutionPath() + "/" + p + "/" + name + ".out";

        try {
            File inFile = new File(inPath);
            FileUtils.copyInputStreamToFile(inputFileResource.getInputStream(), inFile);
            File outFile = new File(outPath);
            FileUtils.copyInputStreamToFile(outputFileResource.getInputStream(), outFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Map<String, String> map = new HashMap<>(2);
        map.put(SOLUTION_STD_IN_PATH_KEY, inPath);
        map.put(SOLUTION_EXPECTED_STD_OUT_PATH_KEY, outPath);
        return map;
    }

    /**
     * 比较用户输出和期望输出
     *
     * @param submissionOutput 用户提交的输出文件路径
     * @param expectedOutput   期望输出文件路径
     * @return Boolean 输出是否相同
     * @author yuzhanglong
     * @date 2020-6-24 12:20:43
     */

    private Boolean compareOutputWithResolutions(String submissionOutput, String expectedOutput) {
        String exitCode = "0";
        try {
            String compareScript = JudgeHolder.getCompareScriptPath();

            Process process = JudgeHolder.getRunner().exec(new String[]{
                    compareScript,
                    submissionOutput,
                    expectedOutput
            });
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            exitCode = reader.readLine();
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        return "0".equals(exitCode);
    }

    /**
     * 调用判题核心，执行判题
     *
     * @param stdInPath 单个输入文件
     * @param name      本次测试点文件名称
     * @return SingleJudgeResultDTO 单次判题结果
     * @author yuzhanglong
     * @date 2020-6-24 12:10:43
     */
    private SingleJudgeResultDTO startJudging(String stdInPath, String name) {
        String judgeCoreScript = JudgeHolder.getJudgeCoreScriptPath();
        JudgeDTO config = JudgeHolder.getJudgeConfig();
        String workingPath = JudgeHolder.getSubmissionWorkingPath();
        String[] command = {
                judgeCoreScript,
                "-r", JudgeHolder.getRunnerScriptPath(),
                "-o", workingPath + "/" + name + ".out",
                "-t", String.valueOf(config.getRealTimeLimit()),
                "-c", String.valueOf(config.getCpuTimeLimit()),
                "-m", String.valueOf(config.getMemoryLimit()),
                "-f", String.valueOf(config.getOutputLimit()),
                "-e", workingPath + "/" + name + ".err",
                "-i", stdInPath,
                "-g", String.valueOf(USE_JUDGE_CORE_GUARD)
        };
        List<String> result = new ArrayList<>();
        try {
            Process process = JudgeHolder.getRunner().exec(command);
            process.waitFor();
            result = readStdout(process);
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        // 将判题核心的stdout转换成数据传输对象
        return JSON.parseObject(DataTransform.stringListToString(result), SingleJudgeResultDTO.class);
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
        return stringList;
    }
}