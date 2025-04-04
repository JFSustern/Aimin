package com.oimc.aimin.admin.service;

import com.oimc.aimin.admin.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oimc.aimin.admin.model.req.CreateRoleReq;
import com.oimc.aimin.admin.model.req.RoleReq;
import com.oimc.aimin.admin.model.req.UpdateRoleReq;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
public interface RoleService extends IService<Role> {

    List<Role> getAllRole();

    /**
     * 给某个角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissionsToRole(Integer roleId, List<Integer> permissionIds);

    List<Role> search(RoleReq req);
    
    /**
     * 创建角色
     * @param req 创建角色请求
     * @return 创建的角色ID
     */
    Integer createRole(CreateRoleReq req);
    
    /**
     * 根据ID删除角色
     * @param id 角色ID
     */
    void deleteRoleById(Integer id);
    
    /**
     * 更新角色信息
     * @param req 更新角色请求
     */
    void updateRole(UpdateRoleReq req);

    /**
     * 根据管理员ID获取角色代码列表
     * 用于权限验证和展示
     *
     * @param adminId 管理员ID
     * @return 角色代码列表
     */
    Set<String> getRoleCodesByAdminId(Integer adminId);

    void clearCache();
}