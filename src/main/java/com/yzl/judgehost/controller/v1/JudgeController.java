package com.yzl.judgehost.controller.v1;

import com.yzl.judgehost.core.authorization.AuthorizationRequired;
import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.exception.http.ForbiddenException;
import com.yzl.judgehost.service.JudgeService;
import com.yzl.judgehost.vo.JudgeConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

/**
 * 判题接口
 *
 * @author yuzhanglong
 * @date 2020-7-6 21:57
 */
@RestController
@RequestMapping("/judge")

public class JudgeController {
    private final JudgeService judgeService;

    @Autowired
    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    /**
     * 执行判题
     *
     * @param judgeDTO 判题相关数据传输对象
     * @author yuzhanglong
     * @date 2020-7-1 21:00
     */
    @PostMapping("/result")
    @AuthorizationRequired
    public JudgeConditionVO runJudge(@RequestBody @Validated JudgeDTO judgeDTO) throws ExecutionException, InterruptedException {
        CompletableFuture<JudgeConditionVO> judgeResults;
        try {
            judgeResults = judgeService.runJudge(judgeDTO);
        } catch (RejectedExecutionException e) {
            throw new ForbiddenException("B1005");
        }
        return judgeResults.get();
    }
}