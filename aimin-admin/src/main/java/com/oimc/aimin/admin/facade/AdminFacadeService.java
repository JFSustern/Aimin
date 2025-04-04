package com.oimc.aimin.admin.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.model.req.SearchReq;
import com.oimc.aimin.admin.service.AdminService;
import com.oimc.aimin.admin.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 *
 * @author 渣哥
 */
@Service
@RequiredArgsConstructor
public class AdminFacadeService {

    private final AdminService adminService;

    private final DepartmentService departmentService;

    public Page<Admin> searchFuzzy(SearchReq req){
        List<Admin> list = adminService.searchFuzzyWithDept(req);
        departmentService.tree(1);
        return null;

    }


}
