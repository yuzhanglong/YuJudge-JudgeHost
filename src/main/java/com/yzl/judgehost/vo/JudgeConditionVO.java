package com.yzl.judgehost.vo;

import com.yzl.judgehost.dto.SingleJudgeResultDTO;

import java.util.List;

/**
 * @author yuzhanglong
 * @date 2020-6-29 22:41:12
 * @description 对某次判题最终结果的表现层对象
 */
public class JudgeConditionVO {
    private final List<SingleJudgeResultDTO> judgeResults;
    private final String submissionId;
    private final Long judgeEndTime;
    private final List<String> extraInfo;


    public List<SingleJudgeResultDTO> getJudgeResults() {
        return judgeResults;
    }

    public Long getJudgeEndTime() {
        return judgeEndTime;
    }


    public JudgeConditionVO(List<SingleJudgeResultDTO> judgeResults, List<String> compileResult, String submissionId) {
        this.judgeResults = judgeResults;
        this.submissionId = submissionId;
        this.extraInfo = compileResult;
        this.judgeEndTime = System.currentTimeMillis();
    }

    public List<String> getExtraInfo() {
        return extraInfo;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    @Override
    public String toString() {
        return "JudgeConditionVO{" +
                "judgeResults=" + judgeResults +
                ", judgeEndTime='" + judgeEndTime + '\'' +
                '}';
    }
}
