package com.oimc.aimin.admin.utils;

import com.oimc.aimin.admin.model.req.PermissionReq;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.model.entity.Permission;
import com.oimc.aimin.admin.model.vo.AdminVO;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.model.vo.PermissionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    List<AdminVO> adminList2VoList(List<Admin> adminList);

    @Mapping(target = "roles", ignore=true)
    AdminVO admin2VO(Admin admin);

    @Mapping(source = "encryptedPassword", target = "password")
    Admin adminContext2Entity(AdminCreateContext context);

    List<PermissionVO> permissionList2VO(List<Permission> permissionList);

    Permission permissionReq2Entity(PermissionReq req);
}
