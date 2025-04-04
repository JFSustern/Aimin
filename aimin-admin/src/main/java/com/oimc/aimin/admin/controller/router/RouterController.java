package com.oimc.aimin.admin.controller.router;

import com.oimc.aimin.admin.model.vo.RouterVO;
import com.oimc.aimin.admin.service.PermissionService;
import com.oimc.aimin.base.resp.Result;
import com.oimc.aimin.satoken.admin.StpAdminUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/router")
public class RouterController {

    private final PermissionService permissionService;

    /**
     * 获取当前登录管理员的路由信息
     * @return Result
     */
    @Operation(summary = "获取当前登录管理员的路由信息", description = "获取当前登录管理员的路由信息，用于前端动态加载路由")
    @GetMapping("/list")
    public Result<?> list(){
        Integer adminId = StpAdminUtil.getLoginId();
        List<RouterVO> routers = permissionService.getRoutersByAdminId(adminId);
        return Result.success(routers);
    }

}
