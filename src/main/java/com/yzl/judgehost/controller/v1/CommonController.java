package com.yzl.judgehost.controller.v1;

import com.github.dozermapper.core.Mapper;
import com.yzl.judgehost.bo.JudgeHostConditionBO;
import com.yzl.judgehost.bo.JudgeHostConfigurationBO;
import com.yzl.judgehost.core.common.UnifiedResponse;
import com.yzl.judgehost.service.CommonService;
import com.yzl.judgehost.vo.JudgeConditionVO;
import com.yzl.judgehost.vo.JudgeHostConditionVO;
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
    private final Mapper mapper;

    public CommonController(CommonService commonService, Mapper mapper) {
        this.commonService = commonService;
        this.mapper = mapper;
    }

    /**
     * @author yuzhanglong
     * @date 2020-8-17 19:40:42
     * @description 测试连接，并返回判题机相关信息和状态
     */
    @GetMapping("/test_connection")
    private UnifiedResponse testConnection() {
        JudgeHostConfigurationBO judgeHostInfo = commonService.getJudgeHostConfiguration();
        JudgeHostConditionBO conditionBO = commonService.getJudgeHostCondition();
        JudgeHostConditionVO conditionVO = mapper.map(conditionBO, JudgeHostConditionVO.class);
        conditionVO.setPort(judgeHostInfo.getPort());
        conditionVO.setScriptPath(judgeHostInfo.getScriptPath());
        conditionVO.setResolutionPath(judgeHostInfo.getResolutionPath());
        conditionVO.setWorkPath(judgeHostInfo.getWorkPath());
        return new UnifiedResponse("judgeHost运行正常", conditionVO);
    }
}
