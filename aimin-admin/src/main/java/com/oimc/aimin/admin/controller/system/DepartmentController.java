package com.oimc.aimin.admin.controller.system;

import cn.hutool.core.lang.tree.Tree;
import com.oimc.aimin.admin.service.DepartmentService;
import com.oimc.aimin.base.resp.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dept")
public class DepartmentController {
    private final DepartmentService departmentService;
    
    @GetMapping("/tree")
    public Result<List<Tree<Integer>>> tree(Integer id){
        List<Tree<Integer>> tree = departmentService.tree(id);
        return Result.success(tree);
    }
}
