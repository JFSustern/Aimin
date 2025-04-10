package com.oimc.aimin.drug.facade;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.drug.service.DrugCategoryService;
import com.oimc.aimin.drug.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 *
 * @author 渣哥
 */
@Service
@RequiredArgsConstructor
public class DrugFacadeService {

    private final DrugService drugService;

    private final DrugCategoryService drugCategoriesService;

    public List<Tree<Integer>> getCategoryTree(Integer id){
        if (null == id){
            id = 0;
        }
        List<DrugCategory> drugCategories = drugCategoriesService.cacheDeepGetAll();
        return buildTree(drugCategories, id);
    }

    private List<Tree<Integer>> buildTree(List<DrugCategory> list, Integer id) {
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都有默认值的
        treeNodeConfig.setWeightKey("categoryId");
        // 最大递归深度
        treeNodeConfig.setDeep(5);

        return TreeUtil.build(list, id,treeNodeConfig, (node, tree) -> {
            tree.setId(node.getCategoryId());
            tree.setParentId(node.getParentId());
            tree.setWeight(node.getCategoryId());
            tree.setName(node.getName());
        });
    }


}
