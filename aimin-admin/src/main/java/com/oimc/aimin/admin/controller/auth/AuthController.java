package com.oimc.aimin.admin.controller.auth;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.oimc.aimin.admin.model.req.LoginReq;
import com.oimc.aimin.admin.model.vo.LoginVO;
import com.oimc.aimin.admin.service.AdminService;
import com.oimc.aimin.admin.service.impl.CaptchaService;
import com.oimc.aimin.base.resp.Result;
import com.oimc.aimin.satoken.admin.StpAdminUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
public class AuthController {

    private final AdminService adminService;

    private final CaptchaService captchaService;

    @Operation
    @PostMapping("/token")
    public Result<?> token(@RequestBody LoginReq login) {
        Boolean verify = captchaService.verify(login.getCaptchaVerifyParam());
        LoginVO loginVO = new LoginVO();
        if (!verify) {
            loginVO.setCaptchaVerifyResult(false);
            return Result.success(loginVO);
        }
        SaTokenInfo tokenInfo = adminService.login(login);
        List<String> permissions = StpAdminUtil.permissions();
        loginVO.setCaptchaVerifyResult(true);
        loginVO.setTokenInfo(tokenInfo);
        loginVO.setPermissions(permissions);
        return Result.success(loginVO);
    }

    // 登出接口
    @GetMapping("/logout")
    public Result<?> logout() {
        StpAdminUtil.logout();
        return Result.success();
    }

}
