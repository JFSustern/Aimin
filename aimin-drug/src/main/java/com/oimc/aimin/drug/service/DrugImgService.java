package com.oimc.aimin.drug.service;

import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.cache.formal.service.BaseCacheService;

import java.util.List;

/**
 * 药品图片服务接口
 * @author 渣哥
 */
public interface DrugImgService extends BaseCacheService<DrugImg> {


    
    /**
     * 设置药品主图
     *
     * @param imageId 图片ID
     * @return 是否设置成功
     */
    boolean setMainImage(Integer imageId);
    
    /**
     * 获取药品的所有图片
     *
     * @param drugId 药品ID
     * @return 图片列表
     */
    List<DrugImg> listByDrugId(Integer drugId);

    /**
     * 根据图片路径更新图片信息
     *
     * @param drugImg 图片信息
     */
    void updateByPath(DrugImg drugImg);
}
