package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.bo.JudgeHostConfigurationBO;
import com.yzl.judgehost.core.common.UnifiedResponse;
import com.yzl.judgehost.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuzhanglong
 * @date 2020-7-30 19:47
 * @description 一般功能性接口
 */

@RestController
@RequestMapping("/common")
public class CommonController {
    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/test_connection")
    private UnifiedResponse testConnection() {
        JudgeHostConfigurationBO judgeHostInfo = commonService.getJudgeHostBasicInfo();
        return new UnifiedResponse("judgeHost运行正常", judgeHostInfo);
    }
}
