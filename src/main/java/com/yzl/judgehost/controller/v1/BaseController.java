package com.yzl.judgehost.controller.v1;

import com.yzl.judgehost.core.common.UnifiedResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 增加 baseController
 *
 * @author yuzhanglong
 * @date 2020-9-13 17:51:40
 */

@RestController
public class BaseController {

    /**
     * 欢迎 -- 表示部署成功
     *
     * @author yuzhanglong
     * @date 2020-9-13 17:56:20
     */

    @GetMapping("/")
    public UnifiedResponse getWelcomeText() {
        return new UnifiedResponse("Your project is running successfully! O(∩_∩)O");
    }
}
