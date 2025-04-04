package com.oimc.aimin.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.model.req.CreateAdminReq;
import com.oimc.aimin.admin.model.req.SearchReq;
import com.oimc.aimin.admin.model.req.UpdateAdminReq;
import com.oimc.aimin.admin.model.vo.AdminVO;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.service.AdminService;
import com.oimc.aimin.admin.service.pipeline.delete.context.AdminDelContext;
import com.oimc.aimin.admin.utils.ObjectConvertor;
import com.oimc.aimin.base.resp.PageResp;
import com.oimc.aimin.base.resp.Result;
import com.oimc.aimin.satoken.admin.StpAdminUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 管理员控制器
 * 提供管理员的增删改查等操作
 */
@Tag(name = "管理员管理", description = "管理员的创建、查询、更新和删除")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/admin")
public class AdminController {

    private final AdminService adminService;

    private final ObjectConvertor objectConvertor;


    /**
     * 创建管理员
     * 创建一个新的管理员账号，包括基本信息和角色分配
     *
     * @param createRequest 创建管理员的请求对象，包含用户名、密码等信息
     * @return 操作结果
     */
    @Operation(summary = "创建管理员", description = "创建一个新的管理员账号，包括设置基本信息和分配角色权限")
    @SaCheckPermission("system:user:add")
    @PostMapping
    public Result<?> createAdmin(@RequestBody @Valid CreateAdminReq createRequest){
        adminService.add(new AdminCreateContext(createRequest));
        return Result.success();
    }

    /**
     * 批量删除管理员
     * 根据ID集合批量删除管理员账号
     *
     * @param adminIds 管理员ID集合
     * @return 操作结果
     */
    @Operation(summary = "批量删除管理员", description = "根据ID集合批量删除管理员，同时移除关联的角色分配")
    @SaCheckPermission("system:user:delete")
    @DeleteMapping
    public Result<?> deleteAdmins(@RequestBody @NotEmpty Set<Integer> adminIds){
        adminService.delete(new AdminDelContext(adminIds));
        return Result.success();
    }

    /**
     * 更新管理员信息
     * 更新管理员的基本信息或角色分配
     *
     * @param updateRequest 更新管理员的请求对象，包含ID和需要更新的字段
     * @return 操作结果
     */
    @Operation(summary = "更新管理员", description = "更新管理员信息，可包括基本资料修改和角色重新分配")
    @SaCheckPermission("system:user:edit")
    @PutMapping
    public Result<?> updateAdmin(@RequestBody @Valid UpdateAdminReq updateRequest) {
        adminService.updateAdminInfo(updateRequest);
        return Result.success();
    }

    /**
     * 搜索管理员
     * 根据条件分页查询管理员列表
     *
     * @param searchRequest 搜索条件，包含关键字和分页信息
     * @return 分页的管理员列表
     * @throws ExecutionException 执行异常
     * @throws InterruptedException 中断异常
     */
    @Operation(summary = "搜索管理员", description = "根据条件分页查询管理员列表，支持关键字搜索和分页")
    @SaCheckPermission("system:user:list")
    @PostMapping("/search")
    public Result<?> searchAdmins(@RequestBody SearchReq searchRequest) throws ExecutionException, InterruptedException {
        Page<Admin> adminPage = adminService.search(searchRequest);
        PageResp<AdminVO> response = PageResp.build(adminPage, AdminVO.class);
        return Result.success(response);
    }

    /**
     * 获取单个管理员详情
     * 根据ID获取管理员的详细信息，包括关联的角色
     * 
     * @param id 管理员ID
     * @return 管理员详情
     */
    @Operation(summary = "获取管理员详情", description = "根据ID获取单个管理员的详细信息，包括基本资料和已分配角色")
    @SaCheckPermission("system:user:info")
    @GetMapping("/{id}")
    public Result<?> getAdminDetail(@PathVariable Integer id) {
        Admin admin = adminService.getAllInfoByAdminId(id);
        return Result.success(admin);
    }

    @GetMapping("/info")
    public Result<?> info() {
        Integer loginId = StpAdminUtil.getLoginId();
        Admin admin = adminService.getAdminById(loginId);
        AdminVO adminVO = objectConvertor.admin2VO(admin);
        return Result.success(adminVO);
    }
    /**
     * 获取管理员信息
     * 获取管理员的详细信息，包括基本信息和关联的角色
     *
     * @return 管理员详情
     */
    @GetMapping("/deep/{id}")
    public Result<?> deep(@PathVariable("id") Integer id) {
        Admin byIdDeep = adminService.getByIdDeep(id);
        return Result.success(byIdDeep);
    }
}