package com.oimc.aimin.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.admin.mapper.AdminRoleMapper;
import com.oimc.aimin.admin.mapper.DepartmentMapper;
import com.oimc.aimin.admin.mapper.RoleMapper;
import com.oimc.aimin.admin.model.entity.Role;
import com.oimc.aimin.admin.model.entity.Department;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.model.req.SearchReq;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

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

    private final RoleMapper roleMapper;

    private final AdminRoleMapper adminRoleMapper;

    private final DepartmentMapper departmentMapper;

    private final ExecutorService executorService;

    private final List<AdminCreateHandler> createHandlers;

    private final List<AdminDeleteHandler> deleteHandlers;


    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void add(AdminCreateContext context) {
        createHandlers.forEach(handler -> {
            handler.handle(context);
        });
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void delete(AdminDelContext context) {
        deleteHandlers.forEach(handler -> {
            handler.handle(context);
        });
    }

    @Override
    public SaTokenInfo login(LoginReq loginModel) {
        // 1. 使用LambdaQueryWrapper
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, loginModel.getUsername());

        // 2. 明确命名查询结果
        Admin adminUser = getOne(queryWrapper);
        if (adminUser == null) {
            throw BusinessException.of("用户名或密码错误");
        }

        // 3. 密码验证前置条件检查
        if (loginModel.getPassword() == null || adminUser.getPassword() == null) {
            throw BusinessException.of("密码校验参数缺失");
        }

        // 4. 密码验证逻辑
        Boolean verified = PwdUtils.verifyBCryptHash(loginModel.getPassword(), adminUser.getPassword());
        if (!verified) {
            throw BusinessException.of("用户名或密码错误");
        }

        return StpAdminUtil.loginByPc(adminUser.getId());
    }

    /**
     * 分页模糊查询管理员
     * @param req 分页搜索model
     * @return 分页结果
     */
    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public Page<Admin> search(SearchReq req) throws ExecutionException, InterruptedException {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<Admin>();
        if (StringUtils.isNotEmpty(req.getContent())) {
            // 模糊查询用户名、昵称和手机号
            queryWrapper.and(wrapper ->
                    wrapper.like(Admin::getUsername, req.getContent())
                            .or()
                            .like(Admin::getNickname, req.getContent())
                            .or()
                            .like(Admin::getPhone, req.getContent())
            );
        }
        if(req.getDeptId() != null){
            Set<Integer> deptIds = extractChildrenIds(req.getDeptId());
            queryWrapper.in(Admin::getDeptId, deptIds);
        }


        Page<Admin> page = page(new Page<>(req.getPageIndex(), req.getPageSize()), queryWrapper);
        List<Admin> records = addAdminAttribute(page);
        page.setRecords(records);
        return page;
    }
    @Override
    public List<Admin> searchFuzzyWithDept(SearchReq req) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<Admin>();
        if (StringUtils.isNotEmpty(req.getContent())) {
            // 模糊查询用户名、昵称和手机号
            queryWrapper.and(wrapper ->
                    wrapper.like(Admin::getUsername, req.getContent())
                            .or()
                            .like(Admin::getNickname, req.getContent())
                            .or()
                            .like(Admin::getPhone, req.getContent())
            );
        }
        if(req.getDeptId() != null){
            Set<Integer> deptIds = extractChildrenIds(req.getDeptId());
            queryWrapper.in(Admin::getDeptId, deptIds);
        }
        return list(queryWrapper);
    }


    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public Admin getAllInfoByAdminId(Integer adminId) {
        if (adminId == null) {
            throw BusinessException.of("管理员ID不能为空");
        }
        // 获取管理员完整信息，包括角色和权限
        Admin admin = baseMapper.getAdminWithRolesAndPermissions(adminId);
        if (admin == null) {
            throw BusinessException.of("管理员不存在");
        }
        return admin;
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void updateAdminInfo(UpdateAdminReq req) {
        Integer adminId = req.getAid();
        // 1. 检查用户是否存在
        Admin admin = getById(adminId);
        if (admin == null) {
            throw BusinessException.of("管理员不存在");
        }
        // 2. 检查用户名是否被其他用户占用
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, req.getUsername())
                    .ne(Admin::getId,adminId);
        if (count(queryWrapper) > 0) {
            throw BusinessException.of("用户名已被占用");
        }
        // 3. 检查手机号是否被其他用户占用
        if (StringUtils.isNotEmpty(req.getPhone())) {
            LambdaQueryWrapper<Admin> phoneQuery = new LambdaQueryWrapper<>();
            phoneQuery.eq(Admin::getPhone, req.getPhone())
                      .ne(Admin::getId, adminId);
            if (count(phoneQuery) > 0) {
                throw BusinessException.of("手机号已被占用");
            }
        }
        
        // 4. 更新管理员基本信息
        Admin updateAdmin = new Admin();
        updateAdmin.setId(adminId);
        updateAdmin.setUsername(req.getUsername());
        updateAdmin.setNickname(req.getNickname());
        updateAdmin.setGender(req.getGender());
        updateAdmin.setPhone(req.getPhone());
        updateAdmin.setStatus(req.getStatus());
        updateAdmin.setDeptId(req.getDeptId());
        updateById(updateAdmin);

        List<Integer> roleIds = req.getRoleIds();
        // 5. 更新管理员角色关联
        if ( CollectionUtils.isNotEmpty(roleIds)) {
            // 先删除现有角色关联
            LambdaQueryWrapper<AdminRole> roleQuery = new LambdaQueryWrapper<>();
            roleQuery.eq(AdminRole::getAdminId, adminId);
            adminRoleMapper.delete(roleQuery);
            // 添加新的角色关联
            List<AdminRole> adminRoles = new ArrayList<>();
            for (Integer roleId : req.getRoleIds()) {
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                adminRoles.add(adminRole);
            }
            adminRoleMapper.insert(adminRoles);
        }
    }

    @Override
    @CacheEvict(value= CACHE_NAME, allEntries = true)
    public void clearCache() {
    }

    /**
     * 根据ID获取管理员昵称
     * 只返回nickname字段，适用于只需要展示用户昵称的场景
     *
     * @param adminId 管理员ID
     * @return 包含昵称的Admin对象，如果未找到则返回null
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Admin getAdminById(Integer adminId) {
        // 使用LambdaQueryWrapper只查询nickname字段
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getId, adminId);
        
        return getOne(queryWrapper);
    }



    private List<Admin> addAdminAttribute(Page<Admin> page) throws ExecutionException, InterruptedException {
        List<Admin> records = page.getRecords();
        if (records.isEmpty()) {
            return records;
        }
        for (Admin admin : records) {
            CompletableFuture<List<Role>> rolesFuture = CompletableFuture.supplyAsync(
                    () -> {
                        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                                .selectAll(Role.class)
                                .leftJoin(AdminRole.class, AdminRole::getRoleId, Role::getId)
                                .eq(AdminRole::getAdminId, admin.getId());
                        return roleMapper.selectJoinList(Role.class, wrapper);
                    },
                    executorService
            );

            CompletableFuture<Department> deptFuture = CompletableFuture.supplyAsync(
                    () -> departmentMapper.selectById(admin.getDeptId()),
                    executorService
            );

            // 等待两个异步任务完成并设置结果
            CompletableFuture.allOf(rolesFuture, deptFuture).join();
            admin.setRoles(rolesFuture.get());
            admin.setDepartment(deptFuture.get());

        }

        return records;
    }

    private Set<Integer> extractChildrenIds(Integer deptId) {
        Set<Integer> deptIds = new HashSet<>();
        if (deptId == null) {
            return deptIds;
        }

        // 添加当前部门ID
        deptIds.add(deptId);

        // 递归查找所有子部门
        findChildDepts(deptId, deptIds);

        return deptIds;
    }

    /**
     * 递归查找子部门ID
     * @param parentId 父部门ID
     * @param deptIds 存储部门ID的集合
     */
    private void findChildDepts(Integer parentId, Set<Integer> deptIds) {
        // 查询所有parentId为指定值的部门
        List<Department> childDepts = departmentMapper.selectList(new LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, parentId));

        // 如果没有子部门，直接返回
        if (childDepts == null || childDepts.isEmpty()) {
            return;
        }

        // 遍历子部门
        for (Department dept : childDepts) {
            Integer childId = dept.getId();
            // 添加子部门ID
            deptIds.add(childId);
            // 递归查找子部门的子部门
            findChildDepts(childId, deptIds);
        }
    }

}
