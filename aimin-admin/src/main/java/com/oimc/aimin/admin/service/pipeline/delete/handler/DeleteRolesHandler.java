package com.oimc.aimin.admin.service.pipeline.delete.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oimc.aimin.admin.mapper.AdminRoleMapper;
import com.oimc.aimin.admin.model.entity.AdminRole;
import com.oimc.aimin.admin.service.pipeline.delete.AdminDeleteHandler;
import com.oimc.aimin.admin.service.pipeline.delete.context.AdminDelContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/*
 *
 * @author 渣哥
 */

@Component
@Order(30)
@RequiredArgsConstructor
public class DeleteRolesHandler implements AdminDeleteHandler {

    private final AdminRoleMapper adminRoleMapper;

    @Override
    public void handle(AdminDelContext context) {
        Set<Integer> adminIds = context.getAdminIds();
        LambdaQueryWrapper<AdminRole> eq = new LambdaQueryWrapper<AdminRole>()
                .in(AdminRole::getAdminId, adminIds);
        adminRoleMapper.delete(eq);
    }
}
