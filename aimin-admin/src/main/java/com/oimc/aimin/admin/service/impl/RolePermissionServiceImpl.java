package com.oimc.aimin.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oimc.aimin.admin.mapper.RoleMapper;
import com.oimc.aimin.admin.model.entity.RolePermission;
import com.oimc.aimin.admin.mapper.RolePermissionMapper;
import com.oimc.aimin.admin.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.admin.mapper.PermissionMapper;
import com.oimc.aimin.admin.model.entity.Role;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private static final String CACHE_NAME = "rolePermission";

    /**
     * 为角色分配权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public void assignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        // 1. 验证角色和权限
        validateRoleAndPermissions(roleId, permissionIds);
        
        // 2. 删除该角色已有的所有权限关联
        LambdaQueryWrapper<RolePermission> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RolePermission::getRoleId, roleId);
        this.remove(deleteWrapper);
        
        // 3. 如果权限列表为空，则仅删除不添加
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        
        // 4. 批量插入新的角色-权限关联
        List<RolePermission> rolePermissions = new ArrayList<>(permissionIds.size());
        for (Integer permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermId(permissionId);
            rolePermissions.add(rolePermission);
        }
        
        // 5. 批量保存
        this.saveBatch(rolePermissions);
    }
    
    /**
     * 验证角色和权限是否存在
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @throws BusinessException 如果角色不存在或权限不存在
     */
    private void validateRoleAndPermissions(Integer roleId, List<Integer> permissionIds) {
        // 验证角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        // 验证权限是否都存在（如果permissionIds为空，则跳过验证）
        if (!CollectionUtils.isEmpty(permissionIds)) {
            LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Permission::getId, permissionIds);
            List<Permission> permissions = permissionMapper.selectList(queryWrapper);
            
            if (permissions.size() != permissionIds.size()) {
                // 找出不存在的权限ID
                List<Integer> existingPermissionIds = permissions.stream()
                        .map(Permission::getId)
                        .toList();
                
                List<Integer> nonExistingPermissionIds = permissionIds.stream()
                        .filter(id -> !existingPermissionIds.contains(id))
                        .toList();
                
                throw new BusinessException("权限不存在: " + nonExistingPermissionIds);
            }
        }
    }
}
