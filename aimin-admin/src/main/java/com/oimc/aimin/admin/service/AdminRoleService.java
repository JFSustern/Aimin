package com.oimc.aimin.admin.service;

import com.oimc.aimin.admin.model.entity.AdminRole;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.admin.model.entity.Role;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
public interface AdminRoleService {

    List<Role> getRoleListByAid(Integer adminId);

    void deleteByAdminId(Integer adminId);

    void batchDeleteByAdminIds(Collection<Integer> adminIds);

}
