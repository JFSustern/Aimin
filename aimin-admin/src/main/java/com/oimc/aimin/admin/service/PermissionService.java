package com.oimc.aimin.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.model.vo.RouterVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
public interface PermissionService extends MPJDeepService<Permission> {

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByRoleId(Integer roleId);

    /**
     * 构建菜单树
     * @param permissions 权限列表
     * @return 菜单树
     */
    List<Tree<Integer>> buildMenuTree(List<Permission> permissions);

    List<RouterVO> getRoutersByAdminId(Integer adminId);
    
    /**
     * 获取所有权限的树形结构
     * @return 权限树
     */
    List<Tree<Integer>> getAllPermissionsTree();

    List<Tree<Integer>> buildPermissionTreeWithSelected(List<Permission> allPermissions, List<Permission> permissionsByRoleId, Integer pid);
    
    /**
     * 创建新权限
     * @param req 权限创建请求对象
     * @return 新创建的权限ID
     */
    Integer createOrAddPermission(PermissionReq req);

    /**
     * 根据ID删除权限
     * @param id 权限ID
     */
    void deletePermissionById(Integer id);

    List<Permission> getListByPid(Integer pid);

    List<Permission> getAll();

    void clearCache();
    
    /**
     * 根据管理员ID获取权限代码列表
     * @param adminId 管理员ID
     * @return 权限代码列表
     */
    Set<String> getPermCodesByAdminId(Integer adminId);
}
