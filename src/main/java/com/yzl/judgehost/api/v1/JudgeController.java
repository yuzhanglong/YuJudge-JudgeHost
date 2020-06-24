package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.service.JudgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/test")
    public String runJudge() {
        judgeService.initSubmisstionWorkingEnvironment();
        judgeService.buildSubmission();
        return "helloworld";
//        System.out.println(judgeEnvironmentConfiguration.toString());
    }
}
