package com.oimc.aimin.admin.service.pipeline.create.handler;

import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.service.AdminService;
import com.oimc.aimin.admin.service.pipeline.create.AdminCreateHandler;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.utils.ObjectConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(999)
@RequiredArgsConstructor
public class SaveUserHandler implements AdminCreateHandler {

    private final AdminService adminService;

    private final ObjectConvertor objectConvertor;

    @Override
    public void handle(AdminCreateContext context) {
        Admin admin = objectConvertor.toAdmin(context);
        adminService.insert(admin);
    }

}