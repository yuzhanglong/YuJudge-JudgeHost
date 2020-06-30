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
    private final String submisstionId;
    private final Long judgeEndTime;


    public List<SingleJudgeResultDTO> getJudgeResults() {
        return judgeResults;
    }

    public Long getJudgeEndTime() {
        return judgeEndTime;
    }


    public JudgeConditionVO(List<SingleJudgeResultDTO> judgeResults, String submisstionId) {
        this.judgeResults = judgeResults;
        this.submisstionId = submisstionId;
        this.judgeEndTime = System.currentTimeMillis();
    }

    public String getSubmisstionId() {
        return submisstionId;
    }

    @Override
    public String toString() {
        return "JudgeConditionVO{" +
                "judgeResults=" + judgeResults +
                ", judgeEndTime='" + judgeEndTime + '\'' +
                '}';
    }
}
