package com.oimc.aimin.admin.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.admin.model.entity.Department;
import com.oimc.aimin.admin.mapper.DepartmentMapper;
import com.oimc.aimin.admin.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author root
 * @since 2025/03/13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService, MPJDeepService<Department> {

    public static final String CACHE_NAME = "dept";

    @Override
    //@Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Tree<Integer>> tree(Integer id) {
        if (id == null){
            id = 0;
        }
        List<Department> list = this.getList();
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都有默认值的
        treeNodeConfig.setWeightKey("sort");
        // 最大递归深度
        treeNodeConfig.setDeep(4);

        // List<Department> list = list();

        return TreeUtil.build(list, id,treeNodeConfig, (node, tree) -> {
            tree.setId(node.getId());
            tree.setParentId(node.getParentId());
            tree.setWeight(node.getSort());
            tree.setName(node.getName());
        });
    }

    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<Department> getList(){
        return list();
    }

}
