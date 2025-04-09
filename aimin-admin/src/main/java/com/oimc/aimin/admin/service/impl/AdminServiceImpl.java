package com.oimc.aimin.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.admin.mapper.AdminRoleMapper;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.model.req.LoginReq;
import com.oimc.aimin.admin.model.req.UpdateAdminReq;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.mapper.AdminMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.admin.service.AdminService;

import com.oimc.aimin.admin.service.pipeline.create.AdminCreateHandler;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.service.pipeline.delete.AdminDeleteHandler;
import com.oimc.aimin.admin.service.pipeline.delete.context.AdminDelContext;
import com.oimc.aimin.admin.utils.PwdUtils;
import com.oimc.aimin.base.exception.BusinessException;
import com.oimc.aimin.satoken.admin.StpAdminUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/26
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService{

    private final static String CACHE_NAME = "admin";

    private final AdminRoleMapper adminRoleMapper;

    @Override
    public SaTokenInfo login(LoginReq loginReq) {
        // 1. 使用LambdaQueryWrapper
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, loginReq.getUsername());

        // 2. 明确命名查询结果
        Admin adminUser = getOne(queryWrapper);
        if (adminUser == null) {
            throw BusinessException.of("用户名或密码错误");
        }

        // 3. 密码验证前置条件检查
        if (loginReq.getPassword() == null || adminUser.getPassword() == null) {
            throw BusinessException.of("密码校验参数缺失");
        }

        // 4. 密码验证逻辑
        Boolean verified = PwdUtils.verifyBCryptHash(loginReq.getPassword(), adminUser.getPassword());
        if (!verified) {
            throw BusinessException.of("用户名或密码错误");
        }
        return StpAdminUtil.loginByPc(adminUser.getId());
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public Page<Admin> searchFuzzyWithDept(String content, Set<Integer> deptIds,Integer currentPage,Integer pageSize) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<Admin>();
        if (StringUtils.isNotEmpty(content)) {
            // 模糊查询用户名、昵称和手机号
            queryWrapper.and(wrapper ->
                    wrapper.like(Admin::getUsername,content)
                            .or()
                            .like(Admin::getNickname, content)
                            .or()
                            .like(Admin::getPhone, content)
            );
        }
        if(!deptIds.isEmpty()){
            queryWrapper.in(Admin::getDeptId, deptIds);
        }
        Page<Admin> page = new Page<>(currentPage,pageSize);
        return pageDeep(page,queryWrapper);
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Admin> getAll() {
        return list();
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Admin> deepGetAll() {
        return listDeep(new LambdaQueryWrapper<Admin>());
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Admin> getByIds(Collection<Integer> ids) {
        return listByIds(ids);
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Admin> deepGetByIds(Collection<Integer> ids) {
        return listByIdsDeep(ids);
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public Admin getById(Integer id) {
        return getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, id));
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public Admin deepGetById(Integer id) {
        return getByIdDeep(id);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void insert(Admin admin) {
        baseMapper.insert(admin);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void insert(Collection<Admin> admins) {
        baseMapper.insert(admins);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void update(Admin admin) {
        baseMapper.updateById(admin);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void delete(Integer id) {
        baseMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void delete(Collection<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }

    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public boolean isExists(String username, String phone) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username)
                .or()
                .eq(Admin::getPhone,phone);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void deleteRelationRoles(Set<Integer> adminIds) {
        LambdaQueryWrapper<AdminRole> eq = new LambdaQueryWrapper<AdminRole>()
                .in(AdminRole::getAdminId, adminIds);
        adminRoleMapper.delete(eq);
    }

    @Override
    public boolean checkPhoneUsernameUnique(UpdateAdminReq req) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, req.getUsername())
                .or()
                .eq(Admin::getPhone, req.getPhone())
                .ne(Admin::getId,req.getAid());
        return count(queryWrapper) == 0;
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void updateRelationRoles(Integer aid, List<Integer> roleIds) {
        LambdaQueryWrapper<AdminRole> roleQuery = new LambdaQueryWrapper<>();
        roleQuery.eq(AdminRole::getAdminId, aid);
        adminRoleMapper.delete(roleQuery);
        List<AdminRole> adminRoles = new ArrayList<>();
        for (Integer roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(aid);
            adminRole.setRoleId(roleId);
            adminRoles.add(adminRole);
        }
        adminRoleMapper.insert(adminRoles);
    }

    @Override
    public boolean isExists(Integer id) {
        return count(new LambdaQueryWrapper<Admin>().eq(Admin::getId, id)) > 0;
    }
}
