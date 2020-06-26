package com.yzl.judgehost.api.v1;

import com.yzl.judgehost.dto.AuthorizationDTO;
import com.yzl.judgehost.utils.TokenHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuzhanglong
 * @description 权限处理相关接口
 */

@RestController
@RequestMapping("/auth")
@Validated
public class AuthorizationController {
    @GetMapping("/login")
    public String userLogin() {
        return TokenHelper.generateAuthToken("213123123");
    }

    @PostMapping("/check")
    public String checkAuthToken(@RequestBody AuthorizationDTO authorizationDTO) {
        Boolean isPass = TokenHelper.checkAuthToken(authorizationDTO.getToken());
        if (isPass) {
            return "pass";
        } else {
            return "bad";
        }
    }
}