package com.oimc.aimin.drug.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.base.response.FileUploadResult;
import com.oimc.aimin.drug.mapper.DrugImgMapper;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.drug.service.DrugImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 药品图片服务实现类
 * @author 渣哥
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrugImgServiceImpl extends ServiceImpl<DrugImgMapper, DrugImg> implements DrugImgService {

    private final ApplicationContext context;


    public static final String CACHE_NAME = "drugImg";
    private final DrugImgMapper drugImgMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public boolean setMainImage(Integer imageId) {
        if (imageId == null) {
            return false;
        }
        
        // 查询图片信息
        DrugImg drugImg = getById(imageId);
        if (drugImg == null) {
            log.error("设置主图失败，图片不存在：imageId={}", imageId);
            return false;
        }
        
        // 将同一药品的其他图片设置为非主图
        resetMainImage(drugImg.getDrugId());
        
        // 设置当前图片为主图
        LambdaUpdateWrapper<DrugImg> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DrugImg::getId, imageId)
                    .set(DrugImg::getIsMain, (byte) 1)
                    .set(DrugImg::getUpdateAt, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public List<DrugImg> listByDrugId(Integer drugId) {
        if (drugId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<DrugImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DrugImg::getDrugId, drugId)
                   .orderByAsc(DrugImg::getIsMain) // 主图排在最前面
                   .orderByAsc(DrugImg::getSort); // 按排序字段升序排列
        
        return list(queryWrapper);
    }

    @Override
    public void updateByPath(DrugImg drugImg) {
        LambdaUpdateWrapper<DrugImg> updateWrapper = new LambdaUpdateWrapper<DrugImg>()
                .eq(DrugImg::getId, drugImg.getId());
        drugImgMapper.update(drugImg, updateWrapper);
    }

    /**
     * 重置指定药品的所有图片为非主图
     *
     * @param drugId 药品ID
     */
    private void resetMainImage(Integer drugId) {
        LambdaUpdateWrapper<DrugImg> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DrugImg::getDrugId, drugId)
                    .eq(DrugImg::getIsMain, (byte) 1)
                    .set(DrugImg::getIsMain, (byte) 0)
                    .set(DrugImg::getUpdateAt, LocalDateTime.now());
        update(updateWrapper);
    }

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }
}
