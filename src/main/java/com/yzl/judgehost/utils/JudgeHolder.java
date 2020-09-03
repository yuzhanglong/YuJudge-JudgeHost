package com.yzl.judgehost.utils;

import com.yzl.judgehost.bo.JudgeConfigurationBO;
import com.yzl.judgehost.dto.JudgeDTO;

import java.util.List;


/**
 * 判题全局变量（线程隔离）
 *
 * @author yuzhanglong
 * @date 2020-09-01 20:53:38
 */
public class JudgeHolder {
    public static final String COMPILE_SCRIPT_NAME = "compile.sh";
    public static final String JUDGE_CORE_SCRIPT_NAME = "y_judge";
    public static final String COMPARE_SCRIPT_NAME = "compare.sh";
    public static final String CODE_FILE_NAME = "Main";
    public static final String COMPILE_STD_OUT_NAME = "compile.out";
    public static final String COMPILE_STD_ERR_NAME = "compile.err";
    public static final String RUNNER_SCRIPT_NAME = "run";

    private static final ThreadLocal<JudgeConfigurationBO> JUDGE_HOLDER_THREAD_LOCAL = new ThreadLocal<>();

    public static void initJudgeConfiguration(JudgeConfigurationBO judgeConfigurationBO) {
        JUDGE_HOLDER_THREAD_LOCAL.set(judgeConfigurationBO);
    }

    public static void removeThreadLocal() {
        JUDGE_HOLDER_THREAD_LOCAL.remove();
    }

    private static JudgeConfigurationBO getJudgeConfiguration() {
        return JUDGE_HOLDER_THREAD_LOCAL.get();
    }

    public static String getCompileScriptPath() {
        return getJudgeConfiguration().getScriptPath() + "/" + COMPILE_SCRIPT_NAME;
    }

    public static String getJudgeCoreScriptPath() {
        return getJudgeConfiguration().getScriptPath() + "/" + JUDGE_CORE_SCRIPT_NAME;
    }

    public static String getCompareScriptPath() {
        return getJudgeConfiguration().getScriptPath() + "/" + COMPARE_SCRIPT_NAME;
    }

    public static String getRunnerScriptPath() {
        return getSubmissionWorkingPath() + "/" + RUNNER_SCRIPT_NAME;
    }

    public static JudgeDTO getJudgeConfig() {
        return getJudgeConfiguration().getJudgeConfig();
    }

    public static String getCodePath(String extension) {
        return getSubmissionWorkingPath() + "/" + CODE_FILE_NAME + "." + extension;
    }

    public static String getSubmissionId() {
        return getJudgeConfiguration().getSubmissionId();
    }

    public static Runtime getRunner() {
        return getJudgeConfiguration().getRunner();
    }

    public static String getSubmissionWorkingPath() {
        return getJudgeConfiguration().getWorkPath() + "/" + getSubmissionId();
    }

    public static String getResolutionPath() {
        return getJudgeConfiguration().getResolutionPath();
    }

    public static List<String> getExtraInfo() {
        return getJudgeConfiguration().getExtraInfo();
    }

    public static void setExtraInfo(List<String> extraInfo) {
        JudgeConfigurationBO configurationBO = getJudgeConfiguration();
        configurationBO.setExtraInfo(extraInfo);
    }
}
