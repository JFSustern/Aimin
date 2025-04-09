package com.oimc.aimin.admin.utils;

import com.oimc.aimin.admin.model.entity.Role;
import com.oimc.aimin.admin.model.req.CreateRoleReq;
import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.model.req.UpdateAdminReq;
import com.oimc.aimin.admin.model.req.UpdateRoleReq;
import com.oimc.aimin.admin.model.vo.PermissionVO;
import com.oimc.aimin.admin.model.vo.RouterVO;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.admin.model.vo.AdminVO;
import com.oimc.aimin.admin.model.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    AdminVO toAdminVO(Admin admin);

    @Mapping(source = "encryptedPassword", target = "password")
    Admin toAdmin(AdminCreateContext context);

    Admin toAdmin(UpdateAdminReq req);

    Role toRole(CreateRoleReq req);

    Role toRole(UpdateRoleReq req);

    Permission toPermission(PermissionReq req);

    List<PermissionVO> toPermissionVO(List<Permission> permissions);

    PermissionVO toPermissionVO(Permission permission);

    @Mappings({
            @Mapping(source = "path", target = "meta.title"),
            @Mapping(source = "icon", target = "meta.icon"),
            @Mapping(source = "hidden", target = "meta.hidden"),
    })
    RouterVO toRouterVO(Permission permission);
}
