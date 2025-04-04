package com.oimc.aimin.admin.controller.system;

import cn.hutool.core.lang.tree.Tree;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.service.PermissionService;
import com.oimc.aimin.base.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限/菜单管理
 * 权限包含目录、菜单和按钮三种类型
 * 其中目录和菜单类型共同构成前端可访问的页面结构
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/permissions")
@Tag(name = "权限/菜单管理", description = "权限、菜单及按钮的增删改查接口")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取菜单树形结构
     * @return 菜单树
     */
    @Operation(summary = "获取菜单树", description = "获取所有目录和菜单类型的权限，以树形结构展示")
    @GetMapping("/menus/tree")
    public Result<?> getMenuTree() {
        // 获取所有权限
        List<Permission> allPermissions = permissionService.getAll();
        // 构建菜单树
        List<Tree<Integer>> menuTree = permissionService.buildMenuTree(allPermissions);
        return Result.success(menuTree);
    }

    /**
     * 根据ID删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    @Operation(summary = "删除权限", description = "根据ID删除权限/菜单/按钮，会同时删除与角色的关联关系")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Integer id) {
        permissionService.deletePermissionById(id);
        return Result.success();
    }

    /**
     * 获取角色的权限树及选中状态
     * @param roleId 角色ID
     * @return 带选中状态的权限树
     */
    @Operation(summary = "获取角色权限树", description = "获取完整权限树，并标记指定角色已拥有的权限")
    @GetMapping("/role/{roleId}")
    public Result<?> getRolePermissionTree(@PathVariable Integer roleId) {
        List<Permission> permissionsByRoleId = permissionService.getPermissionsByRoleId(roleId);
        List<Permission> allPermissions = permissionService.getAll();
        List<Tree<Integer>> tree = permissionService.buildPermissionTreeWithSelected(allPermissions, permissionsByRoleId, 0);
        return Result.success(tree);
    }

    /**
     * 获取所有权限的树形结构
     * @return 完整权限树
     */
    @Operation(summary = "获取完整权限树", description = "获取所有类型权限（目录、菜单、按钮）的树形结构")
    @GetMapping("/tree")
    public Result<?> getAllPermissionTree(){
        List<Tree<Integer>> permissionsTree = permissionService.getAllPermissionsTree();
        return Result.success(permissionsTree);
    }

    /**
     * 创建权限
     * @param req 权限创建请求
     * @return 新创建的权限ID
     */
    @Operation(summary = "更新/创建权限", description = "更新权限信息，如ID不存在则创建新权限")
    @PutMapping
    public Result<?> addOrUpdate(@RequestBody @Valid PermissionReq req) {
        Integer permissionId = permissionService.createOrAddPermission(req);
        return Result.success(permissionId);
    }

    /**
     * 获取指定父级下的权限列表
     * @param pid 父权限ID
     * @return 权限列表
     */
    @Operation(summary = "获取子权限列表", description = "获取指定父ID下的所有直接子级权限")
    @GetMapping("/parent/{pid}")
    public Result<?> getChildrenByParentId(@PathVariable("pid") Integer pid) {
        List<Permission> list = permissionService.getListByPid(pid);
        return Result.success(list);
    }
}
