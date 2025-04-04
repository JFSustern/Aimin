package com.oimc.aimin.admin.service.pipeline.delete.handler;

import com.oimc.aimin.admin.mapper.AdminRoleMapper;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.service.pipeline.delete.AdminDeleteHandler;
import com.oimc.aimin.admin.service.pipeline.delete.context.AdminDelContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/*
 *
 * @author 渣哥
 */
@Service
@Order(20)
@RequiredArgsConstructor
public class CollectAdminRolesHandler implements AdminDeleteHandler {

    private final AdminRoleMapper adminRoleMapper;

    @Override
    public void handle(AdminDelContext context) {
        Set<Integer> adminIds = context.getAdminIds();
        adminIds.forEach(adminId -> {
            List<AdminRole> adminRoles = adminRoleMapper.selectByMap(new HashMap<>() {{
                put("admin_id", adminId);
            }});
            List<Integer> roleIds = adminRoles.stream().map(AdminRole::getRoleId).toList();
            context.getRoleMap().put(adminId, roleIds);
        });
    }
}
