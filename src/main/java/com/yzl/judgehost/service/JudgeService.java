package com.yzl.judgehost.service;

import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.enumerations.LanguageScriptEnum;
import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author yuzhanglong
 * @description 判题服务模块
 * @date 2020-6-24 12:10:43
 */

@Service
public class JudgeService {
    private final String submissionCode;
    private final String submisstionId;
    private final String language;
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    private final Runtime runner;
    private final String builderPath;
    private final String runningPath;
    private StringBuilder result;

    public StringBuilder getResult() {
        return result;
    }


    public JudgeService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
        this.submissionCode = "print(\"hello world\")";
        this.language = "python";
        this.submisstionId = "123213123123";
        this.runner = Runtime.getRuntime();
        this.builderPath = getSubmitWorkingPath() + "/build.sh";
        this.runningPath = getSubmitWorkingPath() + "/run";
    }


    /**
     * @return void
     * @author yuzhanglong
     * @description 调用判题核心，执行判题
     * @date 2020-6-24 12:10:43
     */
    public void runJudge() {
        String judgeCoreScript = judgeEnvironmentConfiguration.getScriptPath() + "/y_judger";
        String[] command = {
                judgeCoreScript,
                "-r", runningPath,
                "-o", getSubmitWorkingPath() + "/1.out",
                "-e", getSubmitWorkingPath() + "/err.out",
                "-i", "/Users/yuzhanglong/Desktop/YuJudge-Core/tests/hello/hello-python/hello.in"
        };
        try {
            Process process = runner.exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null) {
                output.append(temp);
            }
            while ((temp = errReader.readLine()) != null) {
                output.append(temp);
            }
            result = output;

        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
    }


    /**
     * @return void
     * @author yuzhanglong
     * @description 利用之前生成的build.sh来编译脚本
     * @date 2020-6-24 12:10:43
     */
    public void buildSubmission() {
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
    public void initSubmisstionWorkingEnvironment() {
        // 文件生成脚本
        String savingScrpit = this.judgeEnvironmentConfiguration.getScriptPath() + "/codeSave.sh";

        // 代码文件包
        String submissionPath = getSubmitWorkingPath();

        // 用户代码
        String codePath = submissionPath + "/code." + LanguageScriptEnum.PYTHON.getExtensionName();

        // 编译脚本
        String buildScript = LanguageScriptEnum.PYTHON.getBuildScriptByRunningPath(codePath, runningPath);

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
}