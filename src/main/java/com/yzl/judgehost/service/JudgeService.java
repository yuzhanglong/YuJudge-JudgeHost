package com.yzl.judgehost.service;

import com.alibaba.fastjson.JSON;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.enumerations.JudgeResultEnum;
import com.yzl.judgehost.core.enumerations.LanguageScriptEnum;
import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.dto.ResolutionDTO;
import com.yzl.judgehost.exception.http.NotFoundException;
import com.yzl.judgehost.network.HttpRequest;
import com.yzl.judgehost.dto.SingleJudgeResultDTO;
import com.yzl.judgehost.utils.DataReformat;
import com.yzl.judgehost.utils.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yuzhanglong
 * @description 判题服务模块
 * @date 2020-6-24 12:10:43
 */

@Service
public class JudgeService {
    private String runningPath;
    private String submisstionId;
    private List<String> extraInfo;
    private JudgeDTO judgeConfig;
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    private final Runtime runner;


    public JudgeService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
        this.runner = Runtime.getRuntime();
    }

    public String getSubmisstionId() {
        return submisstionId;
    }

    public void setJudgeConfig(JudgeDTO judgeConfig) {
        this.judgeConfig = judgeConfig;
    }

    /**
     * @param stdInPath 单个输入文件
     * @return SingleJudgeResultDTO 单次判题结果
     * @author yuzhanglong
     * @description 调用判题核心，执行判题
     * @date 2020-6-24 12:10:43
     */
    private SingleJudgeResultDTO startJudging(String stdInPath) {
        String judgeCoreScript = judgeEnvironmentConfiguration.getScriptPath() + "/y_judger";
        String[] command = {
                judgeCoreScript,
                "-r", runningPath,
                "-o", getSubmitWorkingPath() + "/" + UUID.randomUUID() + ".out",
                "-t", String.valueOf(judgeConfig.getRealTimeLimit()),
                "-c", String.valueOf(judgeConfig.getCpuTimeLimit()),
                "-m", String.valueOf(judgeConfig.getMemoryLimit()),
                "-f", String.valueOf(judgeConfig.getOutputLimit()),
                "-e", getSubmitWorkingPath() + "/" + UUID.randomUUID() + ".err",
                "-i", stdInPath
        };
        List<String> result = new ArrayList<>();
        try {
            Process process = runner.exec(command);
            process.waitFor();
            result = readStdout(process);
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        // 将判题核心的stdout转换成数据传输对象
        SingleJudgeResultDTO singleJudgeResult = JSON.parseObject(
                DataReformat.stringListToString(result),
                SingleJudgeResultDTO.class
        );
        // TODO: 打logger
        return singleJudgeResult;
    }

    /**
     * @return String 编译返回的信息，如果没有信息，则编译成功
     * @author yuzhanglong
     * @description 调用compile.sh 生成脚本
     * @date 2020-6-24 12:10:43
     */
    private List<String> compileSubmission() {
        // 编译脚本
        String compileScript = this.judgeEnvironmentConfiguration.getScriptPath() + "/compile.sh";

        if (!FileHelper.isFileIn(compileScript)) {
            throw new NotFoundException("B1002");
        }
        // 获取编程语言
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeConfig.getLanguage());

        // 用户代码
        String codePath = getSubmitWorkingPath() + "/Main." + language.getExtensionName();

        // 对应语言的编译脚本
        String buildScript = language.getBuildScriptByRunningPath(getSubmitWorkingPath(), codePath);
        List<String> result = new ArrayList<>();
        try {
            Process process = runner.exec(
                    new String[]{
                            compileScript,
                            getSubmitWorkingPath(),
                            codePath,
                            judgeConfig.getSubmissionCode(),
                            buildScript
                    });
            process.waitFor();
            result = readStdout(process);
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        return result;
    }

    /**
     * @return String 本次提交的工作目录
     * @author yuzhanglong
     * @date 2020-6-24 12:20:43
     * @description 返回本次提交的工作目录
     */
    private String getSubmitWorkingPath() {
        return judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + submisstionId;
    }

    /**
     * @return String 本次提交解决方案的工作目录
     * @author yuzhanglong
     * @date 2020-6-29 22:14:57
     * @description 返回本次提交的解答目录(即期望输入输出存储的地方)
     */
    private String getSubmitResolutionPath() {
        return judgeEnvironmentConfiguration.getResolutionPath() + "/" + submisstionId;
    }

    /**
     * @param submisstionOutput 用户提交的输出
     * @param expectedOutput    用户期望输出
     * @return Boolean 输出是否相同
     * @author yuzhanglong
     * @date 2020-6-24 12:20:43
     * @description 比较用户输出和期望输出
     */

    private Boolean compareOutputWithResolutions(String submisstionOutput, String expectedOutput) {
        String exitCode = "0";
        try {
            String compareScript = judgeEnvironmentConfiguration.getScriptPath() + "/compare.sh";

            Process process = runner.exec(new String[]{
                    compareScript,
                    submisstionOutput,
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
     * @param resolution 解决方案数据传输对象
     * @return ResolutionDTO 解决方案的文件地址类
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     * @description 获取输入文件和期望的输出文件，供后续判题使用
     */
    private ResolutionDTO getResolutionInputAndOutputFile(ResolutionDTO resolution) {
        String inputFile = resolution.getInput();
        String outputFile = resolution.getExpectedOutput();

        // 下载、获取输入和期望输出
        Resource inputFileResource = HttpRequest.getFile(inputFile);
        Resource outputFileResource = HttpRequest.getFile(outputFile);

        String inPath = getSubmitResolutionPath() + "/" + UUID.randomUUID() + ".in";
        String outPath = getSubmitResolutionPath() + "/" + UUID.randomUUID() + ".out";
        try {
            File inFile = new File(inPath);
            FileUtils.copyInputStreamToFile(inputFileResource.getInputStream(), inFile);
            File outFile = new File(outPath);
            FileUtils.copyInputStreamToFile(outputFileResource.getInputStream(), outFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        resolution.setInput(inPath);
        resolution.setExpectedOutput(outPath);
        return resolution;
    }

    /**
     * @param judgeDTO judgeDTO对象
     * @return List<SingleJudgeResultDTO> 由一个或多个单次判题结果组成的list
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     * @description 执行判题
     */
    public List<SingleJudgeResultDTO> runJudge(JudgeDTO judgeDTO) {
        // 判断配置合法性
        this.judgeEnvironmentConfiguration.checkJudgeEnvironmentBaseFileIn();
        // 为本次提交提供唯一id
        this.submisstionId = UUID.randomUUID().toString();
        // 判题基础配置
        setJudgeConfig(judgeDTO);
        // 设置执行目录
        runningPath = getSubmitWorkingPath() + "/run";
        // 编译用户的提交
        List<String> compileResult = compileSubmission();
        this.extraInfo = compileResult;
        List<SingleJudgeResultDTO> result = new ArrayList<>();
        // 编译阶段成功，开始运行用户代码
        if (isCompileSuccess(compileResult)) {
            List<ResolutionDTO> totalResolution = judgeDTO.getResolutions();
            for (ResolutionDTO resolutionDTO : totalResolution) {
                SingleJudgeResultDTO singleJudgeResult = runForSingleJudge(resolutionDTO);
                boolean isAccept = singleJudgeResult.getCondition().equals(JudgeResultEnum.ACCEPTED.getNumber());
                // 这个测试点没有通过，并且是acm模式
                result.add(singleJudgeResult);
                if (!isAccept && judgeDTO.isAcmMode()) {
                    break;
                }
                // oi模式，继续执行判题
            }
        } else {
            SingleJudgeResultDTO resolution = new SingleJudgeResultDTO();
            resolution.setCondition(JudgeResultEnum.COMPILE_ERROR.getNumber());
            resolution.setMessageWithCondition();
            result.add(resolution);
        }
        return result;
    }


    /**
     * @param singleResolution 用户传入的单次判题的正确解决方案，参见ResolutionDTO类
     * @return SingleJudgeResultDTO 单次判题结果
     * @author yuzhanglong
     * @date 2020-7-1 9:47
     * @description 根据期望数据来执行单次判题
     * @see ResolutionDTO
     */
    private SingleJudgeResultDTO runForSingleJudge(ResolutionDTO singleResolution) {
        ResolutionDTO resolution = getResolutionInputAndOutputFile(singleResolution);
        SingleJudgeResultDTO singleJudgeResult = startJudging(resolution.getInput());
        List<String> judgeCoreStderr = getJudgeCoreStderr(singleJudgeResult.getStderrPath());
        // 没有stderr输出时:
        if (judgeCoreStderr.size() == 0) {
            // 如果通过，将condition设置为 0
            Boolean isPass = compareOutputWithResolutions(singleJudgeResult.getStdoutPath(), singleResolution.getExpectedOutput());
            if (isPass) {
                singleJudgeResult.setCondition(JudgeResultEnum.ACCEPTED.getNumber());
            }
        } else {
            this.extraInfo = judgeCoreStderr;
            singleJudgeResult.setCondition(JudgeResultEnum.RUNTIME_ERROR.getNumber());
        }
        singleJudgeResult.setMessageWithCondition();
        return singleJudgeResult;
    }

    /**
     * @param process 运行的进程对象
     * @return String 进程输出
     * @throws IOException an I/O exception
     * @author yuzhanglong
     * @date 2020-6-30 21:21
     * @description 获取运行的脚本/可执行文件的输出
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
            // TODO:处理错误输出
            stringList.add(temp);
        }
        // TODO:处理错误输出
        return stringList;
    }

    /**
     * @param compileResult 编译结果
     * @return Boolean 编译是否成功
     * @author yuzhanglong
     * @date 2020-6-30 21:21
     * @description 传入编译结果，根据语言特性来判断编译是否成功
     */
    private Boolean isCompileSuccess(List<String> compileResult) {
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeConfig.getLanguage());
        // c语言家族（c && cpp）
        boolean isCppFamily = (language == LanguageScriptEnum.C || language == LanguageScriptEnum.C_PLUS_PLUS);
        // java
        boolean isJava = (language == LanguageScriptEnum.JAVA);
        // 另外，python 属于解释性语言，不在此处考虑
        for (String str : compileResult) {
            System.out.println("===" + str);
            boolean isbad = str.contains("error:") || str.contains("错误：");
            if (isCppFamily && isbad) {
                return false;
            }
            isbad = str.contains("Error:") || str.contains("错误:");
            if (isJava && isbad) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return List<String> 错误内容，我们用数组存储，用下标来代表行
     * @author yuzhanglong
     * @date 2020-7-1 9:22
     * @description 编译错误会返回错误，类似的，我们在运行判题核心时也可能产生错误
     * 例如：python（解释性语言）的运行错误提示
     */
    private List<String> getJudgeCoreStderr(String stderrPath) {
        List<String> judgeErrors = null;
        try {
            judgeErrors = FileHelper.readFileByLines(stderrPath);
        } catch (IOException ioException) {
            //TODO:找不到stderr的错误处理
            ioException.printStackTrace();
        }
        return judgeErrors;
    }

    public List<String> getExtraInfo() {
        return extraInfo;
    }
}