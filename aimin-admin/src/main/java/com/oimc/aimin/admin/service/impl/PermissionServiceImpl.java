package com.oimc.aimin.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.admin.common.constant.PermissionConstants;
import com.oimc.aimin.admin.common.enums.EnableStatusEnum;
import com.oimc.aimin.admin.model.entity.*;
import com.oimc.aimin.admin.mapper.PermissionMapper;
import com.oimc.aimin.admin.mapper.RolePermissionMapper;
import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.model.vo.RouterVO;
import com.oimc.aimin.admin.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.admin.utils.ObjectConvertor;
import com.oimc.aimin.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    public static final String CACHE_NAME = "perm";
    private final ObjectConvertor objectConvertor;

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return List<Permission>
     */
    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        // 直接通过中间表过滤（无需关联 Role 表）
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<Permission>()
                .selectAll(Permission.class)
                .leftJoin(RolePermission.class, RolePermission::getPermId, Permission::getId)
                .eq(RolePermission::getRoleId, roleId);
        return permissionMapper.selectJoinList(Permission.class, wrapper);
    }

    /**
     * 根据管理员ID获取路由列表,供前端进行动态渲染
     * @param aid 管理员ID
     * @return List<RouterVO>
     */
    @Override
    public List<RouterVO> getRoutersByAdminId(Integer aid) {
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Permission.class)
                .innerJoin(RolePermission.class, RolePermission::getPermId, Permission::getId)
                .innerJoin(AdminRole.class, AdminRole::getRoleId, RolePermission::getRoleId)
                .eq(AdminRole::getAdminId, aid);
        List<Permission> permissions = permissionMapper.selectJoinList(Permission.class, wrapper);

        return buildRouters(permissions);
    }

    /**
     * 获取全部权限信息tree，用于前端展示全部权限信息
     * @return List<Tree<Integer>>
     */
    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Tree<Integer>> getAllPermissionsTree() {
        // 配置树节点
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 设置排序字段
        treeNodeConfig.setWeightKey("sort");
        // 设置ID字段
        treeNodeConfig.setIdKey("id");
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        
        // 获取所有权限
        List<Permission> permissions = list();
        
        // 使用TreeUtil构建树
        return TreeUtil.build(permissions, 0, treeNodeConfig, (permission, tree) -> {
            tree.setId(permission.getId());
            tree.setParentId(permission.getParentId() != null ? permission.getParentId() : 0);
            tree.setWeight(permission.getSort());
            tree.setName(permission.getTitle());
        });
    }


    /**
     * 构建权限树，并设置选中状态
     * @param allPermissions 全部权限列表
     * @param permissionsByRoleId 角色拥有的权限列表
     * @param pid 父节点ID
     * @return 权限树
     */
    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Tree<Integer>> buildPermissionTreeWithSelected(List<Permission> allPermissions, List<Permission> permissionsByRoleId, Integer pid) {
        Set<Integer> checkedIds = permissionsByRoleId.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        // 配置树节点
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 设置排序字段
        treeNodeConfig.setWeightKey("sort");
        // 设置ID字段
        treeNodeConfig.setIdKey("id");
        // 最大递归深度
        treeNodeConfig.setDeep(3);

        return TreeUtil.build(allPermissions, pid, treeNodeConfig, (permission, tree) -> {
            tree.setId(permission.getId());
            tree.setParentId(permission.getParentId() != null ? permission.getParentId() : 0);
            tree.setWeight(permission.getSort());
            tree.setName(permission.getTitle());

            // 添加额外属性
            tree.putExtra("permKey", permission.getPermKey());
            tree.putExtra("type", permission.getPermType());
            // 添加checked属性
            tree.putExtra("checked", checkedIds.contains(permission.getId()));
        });
    }

    /**
     * 创建新权限
     * @param req 权限创建请求对象
     * @return 新创建的权限ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Integer createOrAddPermission(PermissionReq req) {
        if(req.getId() != null){
            Permission permission = objectConvertor.permissionReq2Entity(req);
            updateById(permission);
            return permission.getId();
        }


        // 校验权限唯一键是否已存在
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getPermKey, req.getPermKey());
        if (count(queryWrapper) > 0) {
            throw new BusinessException("权限标识已存在");
        }
        
        // 如果有父权限ID，验证父权限是否存在
        if (req.getParentId() != null && req.getParentId() > 0) {
            Integer parentId = req.getParentId().intValue();
            if (getById(parentId) == null) {
                throw new BusinessException("父权限不存在");
            }
        }
        
        // 创建新权限
        Permission permission = new Permission();
        permission.setTitle(req.getTitle());
        permission.setPermType(req.getPermType());
        permission.setPermKey(req.getPermKey());
        permission.setPath(req.getPath());
        permission.setRouterName(req.getRouterName());
        
        // 转换父权限ID
        permission.setParentId(req.getParentId() != null ? req.getParentId().intValue() : 0);
        permission.setIcon(req.getIcon());
        permission.setComponent(req.getComponent());
        permission.setRedirect(req.getRedirect());
        permission.setHidden(req.getIsHidden());
        permission.setSort(req.getSort());
        
        // 设置默认值
        permission.setStatus(EnableStatusEnum.ENABLE);
        
        // 保存到数据库
        save(permission);
        return permission.getId();
    }

    /**
     * 根据父ID获取权限列表
     * @param pid 父权限ID
     * @return 权限列表
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Permission> getListByPid(Integer pid) {
        if (pid == null) {
            pid = 0;
        }
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getParentId, pid);
        List<Permission> list = list(queryWrapper);
        list.forEach(permission -> {
            LambdaQueryWrapper<Permission> eq = new LambdaQueryWrapper<Permission>().eq(Permission::getParentId, permission.getId());
            long count = count(eq);
            permission.setHasChildren(count >0);
        });
        return list;
    }

    /**
     * 获取所有权限
     * @return 所有权限列表
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Permission> getAll() {
        return list();
    }

    /**
     * 清除缓存
     */
    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void clearCache() {}

    /**
     * 构建菜单树
     * @param permissions 权限列表
     * @return 菜单树
     */
    @Override
    @Cacheable(value= CACHE_NAME, keyGenerator = "keyGen")
    public List<Tree<Integer>> buildMenuTree(List<Permission> permissions) {
        // 过滤出类型为目录(1)或菜单(2)的权限，并按照排序字段排序
        List<Permission> menuPermissions = permissions.stream()
                .filter(p -> Objects.equals(p.getPermType().getValue(), PermissionConstants.CATALOGUE) ||
                        Objects.equals(p.getPermType().getValue(), PermissionConstants.MENU))
                .sorted(Comparator.comparingInt(Permission::getSort))
                .collect(Collectors.toList());

        // 配置树节点
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setDeep(3);

        // 构建树
        return TreeUtil.build(menuPermissions, 0, treeNodeConfig, (permission, tree) -> {
            tree.setId(permission.getId());
            tree.setParentId(permission.getParentId() != null ? permission.getParentId() : 0);
            tree.setWeight(permission.getSort());
            tree.setName(permission.getTitle());
            tree.putExtra("path", permission.getPath());
            tree.putExtra("component", permission.getComponent());
            tree.putExtra("icon", permission.getIcon());
            tree.putExtra("hidden", permission.getHidden());
        });
    }

    /**
     * 根据ID删除权限
     * @param id 权限ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deletePermissionById(Integer id) {
        // 1. 检查权限是否存在
        Permission permission = getById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        
        // 2. 检查是否有子权限
        LambdaQueryWrapper<Permission> childrenQuery = new LambdaQueryWrapper<>();
        childrenQuery.eq(Permission::getParentId, id);
        long childrenCount = count(childrenQuery);
        if (childrenCount > 0) {
            throw new BusinessException("请先删除子权限");
        }
        
        // 3. 删除权限与角色的关联关系
        LambdaQueryWrapper<RolePermission> rolePermQuery = new LambdaQueryWrapper<>();
        rolePermQuery.eq(RolePermission::getPermId, id);
        rolePermissionMapper.delete(rolePermQuery);
        
        // 4. 删除权限
        removeById(id);
    }

    /**
     * 根据管理员ID获取权限代码列表
     * 
     * @param adminId 管理员ID
     * @return 权限代码列表
     */
    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Set<String> getPermCodesByAdminId(Integer adminId) {
        // 使用MPJLambdaWrapper进行多表关联查询，获取用户的所有权限
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Permission::getPermKey)  // 只查询权限代码字段
               .innerJoin(RolePermission.class, RolePermission::getPermId, Permission::getId)
               .innerJoin(AdminRole.class, AdminRole::getRoleId, RolePermission::getRoleId)
               .eq(AdminRole::getAdminId, adminId)
               .eq(Permission::getStatus, EnableStatusEnum.ENABLE)  // 只获取启用状态的权限
               .groupBy(Permission::getPermKey);  // 去重
               
        List<Permission> permissions = permissionMapper.selectJoinList(Permission.class, wrapper);
        
        // 提取permKey字段并返回
        return permissions.stream()
                .map(Permission::getPermKey)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
    }


    /**
     * 构建前端路由
     * @param allPermissions 管理员所有权限
     * @return 路由列表
     */
    private List<RouterVO> buildRouters(List<Permission> allPermissions) {
        // 过滤出类型为目录(1)或菜单(2)的权限，并按照排序字段排序
        List<Permission> menuPermissions = allPermissions.stream()
                .filter(p -> Objects.equals(p.getPermType().getValue(), PermissionConstants.CATALOGUE) ||
                        Objects.equals(p.getPermType().getValue(), PermissionConstants.MENU) &&
                                p.getStatus() == EnableStatusEnum.ENABLE)  // 合并过滤条件
                .sorted(Comparator.comparingInt(Permission::getSort))
                .collect(Collectors.toList());

        // 构建权限树
        return buildPermissionTree(menuPermissions, 0);
    }

    /**
     * 递归构建权限树
     * @param permissions 权限列表
     * @param parentId 父权限ID
     * @return 路由列表
     */
    private List<RouterVO> buildPermissionTree(List<Permission> permissions, Integer parentId) {
        List<RouterVO> routers = new ArrayList<>();
        // 获取指定父ID的所有权限
        List<Permission> children = permissions.stream()
                .filter(p -> Objects.equals(p.getParentId(), parentId))
                .toList();

        if (!children.isEmpty()) {
            for (Permission permission : children) {
                RouterVO router = convertPermissionToRouter(permission);
                // 递归获取子路由
                List<RouterVO> childRouters = buildPermissionTree(permissions, permission.getId());
                if (!childRouters.isEmpty()) {
                    router.setChildren(childRouters);
                }
                routers.add(router);
            }
        }
        return routers;
    }

    /**
     * 将权限转换为路由对象
     * @param permission 权限
     * @return 路由对象
     */
    private RouterVO convertPermissionToRouter(Permission permission) {
        RouterVO router = new RouterVO();
        router.setPath(permission.getPath());
        router.setComponent(permission.getComponent());
        router.setName(permission.getRouterName());
        router.setRedirect(permission.getRedirect());

        // 设置元数据
        RouterVO.Meta meta = new RouterVO.Meta();
        meta.setTitle(permission.getTitle());
        meta.setIcon(permission.getIcon());
        meta.setHidden(permission.getHidden());
        router.setMeta(meta);
        return router;
    }
}
