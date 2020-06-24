package com.yzl.judgehost.service;

import com.yzl.judgehost.core.configuration.JudgeEnvironmentConfiguration;
import com.yzl.judgehost.core.enumerates.LanguageScriptEnum;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author yuzhanglong
 * @description 判题服务模块
 * @date 2020-6-24 12:10:43
 */

@Service
public class JudgeService {
    private final String submissionCode;
    private final String subisstionId;
    private final String language;
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;


    public JudgeService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
        this.submissionCode = "test";
        this.language = "python";
        this.subisstionId = "123213123123";
    }


    /**
     * @return void
     * @author yuzhanglong
     * @description 执行判题
     * @date 2020-6-24 12:10:43
     */
    public void runJudge() {

    }

    /**
     * @return void
     * @author yuzhanglong
     * @description 利用之前生成的build.sh来编译脚本
     * @date 2020-6-24 12:10:43
     */
    public void buildSubmission() {
        String builderPath = getSubmitWorkingPath() + "/build.sh";
        Runtime runner = Runtime.getRuntime();
        try {
            runner.exec(builderPath);
        } catch (IOException ioException) {
            //TODO：异常处理
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

        // 编译脚本路径
        String buildingScriptPath = submissionPath + "/build.sh";

        // 编译脚本
        String buildScript = LanguageScriptEnum.PYTHON.getBuildScript();

        // Runtime对象，准备执行生成脚本
        Runtime runner = Runtime.getRuntime();
        try {
            String[] command = {savingScrpit, submissionPath, codePath, submissionCode, buildingScriptPath, buildScript};
            runner.exec(command);
        } catch (IOException ioException) {
            //TODO：异常处理
//            System.out.println(ioException);
        }
    }

    /**
     * @return String
     * @author yuzhanglong
     * @date 2020-6-24 12:20:43
     * @description 返回本次提交的工作目录
     */
    private String getSubmitWorkingPath() {
        return judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + subisstionId;
    }
}