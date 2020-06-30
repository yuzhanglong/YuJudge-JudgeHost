package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.dto.JudgeDTO;
import com.yzl.judgehost.dto.SingleJudgeResultDTO;
import com.yzl.judgehost.service.JudgeService;
import com.yzl.judgehost.vo.JudgeConditionVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuzhanglong
 * @description 判题接口（Controller）
 */
@RestController
@RequestMapping("/judge")


public class JudgeController {
    private final JudgeService judgeService;

    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }


    @PostMapping("/run")
    public Object runJudge(@RequestBody @Validated JudgeDTO judgeDTO) {
        List<SingleJudgeResultDTO> judgeResults = judgeService.runJudge(judgeDTO);
        List<String> extraResult = judgeService.getCompileResult();
        return new JudgeConditionVO(judgeResults, extraResult, judgeService.getSubmisstionId());
    }
}