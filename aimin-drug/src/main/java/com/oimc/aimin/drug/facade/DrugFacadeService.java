package com.oimc.aimin.drug.facade;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.drug.service.DrugCategoryService;
import com.oimc.aimin.drug.service.DrugImgService;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author 渣哥
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DrugFacadeService {

    private final DrugCategoryService drugCategoriesService;

    private final ObjectConvertor objectConvertor;

    private final DrugService drugService;

    private final DrugImgService drugImgService;
    
    private final DrugImgFacadeService drugImgFacadeService;

    /**
     * 获取药品分类树
     */
    public List<Tree<Integer>> getCategoryTree(Integer id){
        if (null == id){
            id = 0;
        }
        List<DrugCategory> drugCategories = drugCategoriesService.cacheDeepGetAll();
        return buildTree(drugCategories, id);
    }


    public void insertDrug(DrugRequest request){
        Drug drug = objectConvertor.toDrug(request);
        drugService.cacheInsert(drug);
        List<DrugImg> drugImgList = this.buildDrugImgList(request,drug.getDrugId());
        drugImgService.cacheInsert(drugImgList);
    }

    private List<DrugImg> buildDrugImgList(DrugRequest request, Integer drugId){
        List<DrugImg> drugImgList = new ArrayList<>();
        List<DrugRequest.Img> imgList = request.getImgList();
        if(null != imgList && !imgList.isEmpty()){
            for(DrugRequest.Img img : imgList){
                String path = img.getPath();
                DrugImg drugImg = drugImgFacadeService.getCacheImgUploadResult(path);
                drugImg.setDrugId(drugId);
                drugImg.setIsMain(img.getIsMain());
                drugImgList.add(drugImg);
            }
        }
        return drugImgList;
    }

    /**
     * 更新药品
     */

    public void updateDrug(DrugRequest request){
        Drug drug = objectConvertor.toDrug(request);
        drugService.cacheUpdate(drug);
        List<DrugRequest.Img> imgList = request.getImgList();
        if(null != imgList && !imgList.isEmpty()){
            for (DrugRequest.Img img : imgList) {
                DrugImg cache = drugImgFacadeService.getCacheImgUploadResult(img.getPath());
                if(cache !=null){
                    cache.setDrugId(request.getDrugId());
                    cache.setIsMain(img.getIsMain());
                    cache.setDrugId(request.getDrugId());
                    cache.setSort(img.getSort());
                    drugImgService.cacheInsert(cache);
                }else{
                    DrugImg drugImg = new DrugImg();
                    drugImg.setIsMain(img.getIsMain());
                    drugImg.setDrugId(request.getDrugId());
                    drugImg.setPath(img.getPath());
                    drugImg.setSort(img.getSort());
                    drugImgService.updateByPath(drugImg);
                }
            }
        }
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
