package com.oimc.aimin.admin.service.pipeline.create.handler;


import com.oimc.aimin.admin.mapper.AdminMapper;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.service.pipeline.create.AdminCreateHandler;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@Component
@Order(10)
@RequiredArgsConstructor
public class UsernamePhoneValidationHandler implements AdminCreateHandler {

    private final AdminMapper adminMapper;

    @Override
    public void handle(AdminCreateContext context) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, context.getUsername())
                   .or()
                   .eq(Admin::getPhone, context.getPhone());
        
        Long count = adminMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException("用户名或手机号已存在");
        }
    }
}
