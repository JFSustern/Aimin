package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.entity.DrugCategories;
import com.oimc.aimin.drug.mapper.DrugMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@AllArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements MPJDeepService<Drug>, DrugService {

    private final DrugMapper drugsMapper;

    private final String CACHE_NAME = "drug";

    /**
     * 查询药品列表并关联药品分类
     * @return 药品列表
     */
    @Override
    public List<Drug> findDrugsJoinCategories() {
        MPJLambdaWrapper<Drug> wrapper = new MPJLambdaWrapper<Drug>()
                .selectAll(Drug.class)
                .selectAssociation("c", DrugCategories.class, Drug::getCategories)
                .leftJoin(DrugCategories.class, "c",DrugCategories::getCategoryId, Drug::getCategoryId);
        return drugsMapper.selectJoinList(wrapper);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void insert(Drug entity) {
        save(entity);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void updateByPrimaryKey(Drug entity) {
        updateById(entity);
        getByIdDeep();
        get
    }

}
