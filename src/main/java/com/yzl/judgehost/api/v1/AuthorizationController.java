package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.core.authorization.AuthorizationRequired;
import com.yzl.judgehost.core.common.UnifiedResponse;
import com.yzl.judgehost.dto.AuthorizationDTO;
import com.yzl.judgehost.dto.AccessTokenDTO;
import com.yzl.judgehost.exception.http.ForbiddenException;
import com.yzl.judgehost.service.AuthorizationService;
import com.yzl.judgehost.vo.AuthorizationVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuzhanglong
 * @date 2020-7-2 00:16
 * @description 权限处理相关接口
 */


@RestController
@RequestMapping("/auth")
@Validated
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * @author yuzhanglong
     * @description 获取判题服务器接口调用凭据，以供后续接口调用使用
     * @date 2020-7-1 11:24
     */
    @PostMapping("/get_access_token")
    public AuthorizationVO getAccessToken(@RequestBody @Validated AuthorizationDTO authorizationDTO) {
        String accessToken = authorizationService.getAccessToken(authorizationDTO);
        return new AuthorizationVO(accessToken, authorizationService.getExpiredTime());
    }

    /**
     * @author yuzhanglong
     * @description 验证判题服务器接口调用凭据
     * @date 2020-7-1 11:24
     */
    @PostMapping("/check")
    public UnifiedResponse checkAuthToken(@RequestBody AccessTokenDTO accessTokenDTO) {
        Boolean isPass = authorizationService.checkAccessToken(accessTokenDTO);
        if (!isPass) {
            throw new ForbiddenException("A0003");
        }
        return new UnifiedResponse("00000", "ACCESS_TOKEN_PASS!", null);
    }

    @GetMapping("/test")
    @AuthorizationRequired
    public String helloWorld() {
        return "hello world";
    }
}