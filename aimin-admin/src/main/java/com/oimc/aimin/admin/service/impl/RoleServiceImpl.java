package com.oimc.aimin.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.admin.mapper.RolePermissionMapper;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.model.entity.Role;
import com.oimc.aimin.admin.model.entity.RolePermission;
import com.oimc.aimin.admin.mapper.RoleMapper;
import com.oimc.aimin.admin.model.req.CreateRoleReq;
import com.oimc.aimin.admin.model.req.RoleReq;
import com.oimc.aimin.admin.model.req.UpdateRoleReq;
import com.oimc.aimin.admin.service.RoleService;
import com.oimc.aimin.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RolePermissionMapper rolePermissionMapper;
    private final RoleMapper roleMapper;

    public final static String CACHE_NAME = "role";

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> getAllRole() {
        return list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_NAME, PermissionServiceImpl.CACHE_NAME}, allEntries = true)
    public void assignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        LambdaQueryWrapper<RolePermission> eq = new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(eq);
        List<RolePermission> list = permissionIds.stream().map(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermId(permissionId);
            return rolePermission;
        }).toList();
        rolePermissionMapper.insert(list);
    }

    /**
     * 根据条件搜索角色列表
     * @param req 查询条件
     * @return 角色列表
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> search(RoleReq req) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(req.getContent())) {
            queryWrapper.like(Role::getRoleName, req.getContent())
                    .or()
                    .like(Role::getRoleCode, req.getContent());
        }
        return list(queryWrapper);
    }

    /**
     * 创建角色
     * @param req 创建角色请求
     * @return 角色ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Integer createRole(CreateRoleReq req) {
        // 检查角色名称和角色编码是否已存在
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, req.getRoleName())
                .or()
                .eq(Role::getRoleCode, req.getRoleCode());
        if (count(queryWrapper) > 0) {
            throw new BusinessException("角色名称或角色编码已存在");
        }
        // 创建角色对象
        Role role = new Role();
        role.setRoleName(req.getRoleName());
        role.setRoleCode(req.getRoleCode());
        role.setRoleDesc(req.getRoleDesc());
        // 保存角色
        save(role);
        return role.getId();
    }
    
    /**
     * 根据ID删除角色
     * @param id 角色ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteRoleById(Integer id) {
        // 1. 检查角色是否存在
        Role role = getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        // 2. 删除角色与权限的关联关系
        LambdaQueryWrapper<RolePermission> rolePermQuery = new LambdaQueryWrapper<>();
        rolePermQuery.eq(RolePermission::getRoleId, id);
        rolePermissionMapper.delete(rolePermQuery);
        
        // 3. 删除角色
        removeById(id);
    }
    
    /**
     * 更新角色信息
     * @param req 更新角色请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void updateRole(UpdateRoleReq req) {
        // 1. 检查角色是否存在
        Role existingRole = getById(req.getId());
        if (existingRole == null) {
            throw new BusinessException("角色不存在");
        }
        
        // 2. 检查角色名称和角色编码是否与其他角色冲突
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Role::getId, req.getId())
                .and(q -> q.eq(Role::getRoleName, req.getRoleName())
                        .or()
                        .eq(Role::getRoleCode, req.getRoleCode()));
        if (count(queryWrapper) > 0) {
            throw new BusinessException("角色名称或角色编码已被其他角色使用");
        }
        
        // 3. 更新角色信息
        Role role = new Role();
        role.setId(req.getId());
        role.setRoleName(req.getRoleName());
        role.setRoleCode(req.getRoleCode());
        role.setRoleDesc(req.getRoleDesc());

        // 4. 保存更新后的角色
        updateById(role);
    }

    /**
     * 根据管理员ID获取角色代码列表
     * 用于权限验证和展示
     *
     * @param adminId 管理员ID
     * @return 角色代码列表
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Set<String> getRoleCodesByAdminId(Integer adminId) {
        // 使用MPJLambdaWrapper构建关联查询，避免循环依赖
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Role::getRoleCode)  // 只查询角色代码字段
               .innerJoin(AdminRole.class, AdminRole::getRoleId, Role::getId)
               .eq(AdminRole::getAdminId, adminId);
        List<Role> roles = roleMapper.selectJoinList(Role.class, wrapper);

        // 提取角色代码并返回
        return roles.stream()
                .map(Role::getRoleCode)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void clearCache() {}
}