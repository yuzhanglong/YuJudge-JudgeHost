package com.yzl.judgehost.service;

import com.alibaba.fastjson.JSON;
import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.enumerations.LanguageScriptEnum;
import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.dto.ResolutionDTO;
import com.yzl.judgehost.network.HttpRequest;
import com.yzl.judgehost.dto.SingleJudgeResultDTO;
import org.apache.commons.io.FileUtils;
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
    private String builderPath;
    private String runningPath;


    private String submisstionId;
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    private final Runtime runner;


    private JudgeDTO judgeConfig;

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
     * @return String 判题核心返回的输出
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
//                "-t", String.valueOf(judgeConfig.getRealTimeLimit()),
//                "-c", String.valueOf(judgeConfig.getCpuTimeLimit()),
//                "-m", String.valueOf(judgeConfig.getMemoryLimit()),
//                "-f", String.valueOf(judgeConfig.getOutputLimit()),
                "-e", getSubmitWorkingPath() + "/" + UUID.randomUUID() + ".err",
                "-i", stdInPath
        };
        StringBuilder output = new StringBuilder();
        try {
            Process process = runner.exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String temp;
            while ((temp = reader.readLine()) != null) {
                output.append(temp);
            }
            while ((temp = errReader.readLine()) != null) {
                // TODO:处理错误输出
                output.append(temp);
            }

        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        System.out.println(output.toString());
        // 将判题核心的stdout转换成数据传输对象
        return JSON.parseObject(output.toString(), SingleJudgeResultDTO.class);
    }

    /**
     * @return void
     * @author yuzhanglong
     * @description 利用之前生成的build.sh来编译脚本
     * @date 2020-6-24 12:10:43
     */
    private void buildSubmission() {
        try {
            Process process = runner.exec(builderPath);
            process.waitFor();
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
    }

    /**
     * @return void
     * @author yuzhanglong
     * @date 2020-6-24 12:10:43
     * @description 为提交的代码初始化运行环境，具体包括：
     * 1.在judger工作目录下创建对应的文件夹，名称即本次提交的id
     * 2.在目标文件夹下写入用户的代码
     * 3.创建此代码编译/运行的脚本文件，供后续使用
     */
    private void initSubmisstionWorkingEnvironment() {
        String submissionCode = judgeConfig.getSubmissionCode();
        this.builderPath = getSubmitWorkingPath() + "/build.sh";
        this.runningPath = getSubmitWorkingPath() + "/run";
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeConfig.getLanguage());

        // 文件生成脚本
        String savingScrpit = this.judgeEnvironmentConfiguration.getScriptPath() + "/codeSave.sh";

        // 代码文件包
        String submissionPath = getSubmitWorkingPath();

        // 用户代码
        String codePath = submissionPath + "/Main." + language.getExtensionName();

        // 编译脚本
        String buildScript = language.getBuildScriptByRunningPath(codePath, runningPath);

        // Runtime对象，准备执行生成脚本
        try {
            String[] command = {savingScrpit, getSubmitWorkingPath(), codePath, submissionCode, builderPath, buildScript};
            Process process = runner.exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException ioException) {
            //TODO：异常处理
            ioException.printStackTrace();
        }
    }

    /**
     * @return String
     * @author yuzhanglong
     * @date 2020-6-24 12:20:43
     * @description 返回本次提交的工作目录
     */
    private String getSubmitWorkingPath() {
        return judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + submisstionId;
    }

    /**
     * @return String
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
     * @return String
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
     * @param judgeDTO judgeDTO对象
     * @return void
     * @author yuzhanglong
     * @date 2020-6-27 12:21:43
     * @description 执行判题
     */
    public List<SingleJudgeResultDTO> runJudge(JudgeDTO judgeDTO) {
        this.submisstionId = UUID.randomUUID().toString();
        // 判题基础配置
        setJudgeConfig(judgeDTO);
        // 初始化判题环境
        initSubmisstionWorkingEnvironment();
        // 编译用户的提交
        buildSubmission();
        List<SingleJudgeResultDTO> result = new ArrayList<>();
        judgeDTO.getResolutions().forEach(res -> {
            ResolutionDTO resolution = getResolutionInputAndOutputFile(res);
            SingleJudgeResultDTO singleJudgeResult = startJudging(resolution.getInput());
            Boolean isPass = compareOutputWithResolutions(singleJudgeResult.getStdoutPath(), res.getExpectedOutput());
            // 如果通过，将condition设置为 0
            if (isPass) {
                singleJudgeResult.setCondition(0);
            }
            singleJudgeResult.setMessage();
            result.add(singleJudgeResult);
        });
        return result;
    }

    /**
     * @param resolution 解决方案数据传输对象
     * @return void
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

        // TODO: 将基础路径转移到配置文件中
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
}