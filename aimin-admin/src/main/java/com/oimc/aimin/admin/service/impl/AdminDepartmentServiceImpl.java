package com.oimc.aimin.admin.service.impl;

import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.admin.model.entity.AdminDepartment;
import com.oimc.aimin.admin.mapper.AdminDepartmentMapper;
import com.oimc.aimin.admin.service.AdminDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户部门关联表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/02/27
 */
@Service
public class AdminDepartmentServiceImpl extends ServiceImpl<AdminDepartmentMapper, AdminDepartment> implements AdminDepartmentService, MPJDeepService<AdminDepartment> {

}
