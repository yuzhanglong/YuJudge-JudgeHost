package com.yzl.judgehost.controller.v1;

import com.yzl.judgehost.core.authorization.AuthorizationRequired;
import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.dto.SingleJudgeResultDTO;
import com.yzl.judgehost.exception.http.ForbiddenException;
import com.yzl.judgehost.service.JudgeService;
import com.yzl.judgehost.vo.JudgeConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author yuzhanglong
 * @description 判题接口（Controller）
 * @date 2020-7-6 21:57
 */
@RestController
@RequestMapping("/judge")

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JudgeController {
    private final JudgeService judgeService;

    @Autowired
    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    /**
     * @param judgeDTO 判题相关数据传输对象
     * @author yuzhanglong
     * @description 执行判题
     * @date 2020-7-1 21:00
     */
    @PostMapping("/run")
    @AuthorizationRequired
    public Object runJudge(@RequestBody @Validated JudgeDTO judgeDTO) throws ExecutionException, InterruptedException {
        CompletableFuture<List<SingleJudgeResultDTO>> judgeResults;
        try {
            judgeResults = judgeService.runJudge(judgeDTO);
        } catch (RejectedExecutionException e) {
            throw new ForbiddenException("B1005");
        }
        List<SingleJudgeResultDTO> res = judgeResults.get();
        List<String> extraResult = judgeService.getExtraInfo();
        return new JudgeConditionVO(res, extraResult, judgeService.getSubmissionId());
    }

    /**
     * @param judgeDTO 判题相关数据传输对象
     * @author yuzhanglong
     * @description 执行判题(测试模式)
     * @date 2020-7-6 23:57
     */
    @PostMapping("/run_for_test")
    @AuthorizationRequired
    public Object runJudgeWithoutThreadPoolForTest(@RequestBody @Validated JudgeDTO judgeDTO) {
        List<SingleJudgeResultDTO> judgeResults = judgeService.judgeWithoutThreadPoolForTest(judgeDTO);
        List<String> extraResult = judgeService.getExtraInfo();
        return new JudgeConditionVO(judgeResults, extraResult, judgeService.getSubmissionId());
    }
}