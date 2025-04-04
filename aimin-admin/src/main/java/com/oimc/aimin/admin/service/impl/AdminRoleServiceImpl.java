package com.oimc.aimin.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.model.entity.Role;
import com.oimc.aimin.admin.mapper.AdminRoleMapper;
import com.oimc.aimin.admin.mapper.RoleMapper;
import com.oimc.aimin.admin.service.AdminRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService, MPJDeepService<AdminRole> {

    private final RoleMapper roleMapper;

    private final static String CACHE_NAME = "admin_role";


    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> getRoleListByAid(Integer adminId) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(Role.class)
                .leftJoin(AdminRole.class, AdminRole::getRoleId, Role::getId)
                .eq(AdminRole::getAdminId, adminId);
        return roleMapper.selectJoinList(Role.class, wrapper);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void deleteByAdminId(Integer aid) {
        LambdaQueryWrapper<AdminRole> eq = new LambdaQueryWrapper<AdminRole>()
                .eq(AdminRole::getAdminId, aid);
        remove(eq);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void batchDeleteByAdminIds(Collection<Integer> aids) {
        LambdaQueryWrapper<AdminRole> eq = new LambdaQueryWrapper<AdminRole>()
                .in(AdminRole::getAdminId, aids);
        remove(eq);
    }


}
