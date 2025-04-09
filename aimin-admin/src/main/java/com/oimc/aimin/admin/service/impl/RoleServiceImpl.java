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

import java.util.Collection;
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
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RolePermissionMapper rolePermissionMapper;

    public final static String CACHE_NAME = "role";


    @Override
    public boolean checkNameAndCodeUnique(UpdateRoleReq req) {
        return false;
    }


    @Override
    public boolean isExists(String roleName, String roleCode) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, roleName)
                .or()
                .eq(Role::getRoleCode, roleCode);
        return count(queryWrapper) > 0;
    }



    // 以下是BaseService接口方法的实现
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> getAll() {
        return list();
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> deepGetAll() {
        return listDeep(new LambdaQueryWrapper<Role>());
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> getByIds(Collection<Integer> ids) {
        return listByIds(ids);
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Role> deepGetByIds(Collection<Integer> ids) {
        return listByIdsDeep(ids);
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Role getById(Integer id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Role deepGetById(Integer id) {
        return getByIdDeep(id);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void insert(Role role) {
        save(role);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void insert(Collection<Role> roles) {
        saveBatch(roles);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void update(Role role) {
        updateById(role);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void delete(Integer id) {
        removeById(id);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void delete(Collection<Integer> ids) {
        removeByIds(ids);
    }

    @Override
    public boolean isExists(Integer id) {
        return count(new LambdaQueryWrapper<Role>().eq(Role::getId, id)) > 0;
    }
}