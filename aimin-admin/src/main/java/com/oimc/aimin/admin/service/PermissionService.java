package com.oimc.aimin.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.model.vo.RouterVO;
import jakarta.validation.constraints.Size;

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
public interface PermissionService extends BaseService<Permission> {

    List<Permission> getListByPid(Integer pid);

    // Todo 故意保留，跟Deep查询做对比
    Set<String> getPermCodesByAdminId(Integer adminId);

    List<Permission> getChildren(Integer id);

    boolean isExistsByPermKey(String permKey);
}
