package com.oimc.aimin.drug.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.mapper.DrugMapper;
import com.oimc.aimin.drug.service.DrugService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugsMapper;

    private final ApplicationContext context;

    public static final String CACHE_NAME = "drug";


    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "keyGen")
    public Page<Drug> pageSearchFuzzy(PageDrugRequest page) {
        LambdaQueryWrapper<Drug> queryWrapper = new LambdaQueryWrapper<Drug>();
        if (StrUtil.isNotBlank(page.getContent())){
            queryWrapper.and(wrapper -> {
                wrapper.like(Drug::getName, page.getContent())
                        .or()
                        .like(Drug::getGenericName, page.getContent())
                        .or()
                        .like(Drug::getManufacturer, page.getContent())
                        .or()
                        .like(Drug::getBrand, page.getContent())
                        .or()
                        .like(Drug::getDescription, page.getContent());

            });
        }
        if (page.getCategoryId() != null){
            queryWrapper.eq(Drug::getCategoryId, page.getCategoryId());
        }
        Page<Drug> drugPage = new Page<>(page.getCurrentPage(), page.getPageSize());
        return pageDeep(drugPage, queryWrapper);

    }

    @Override
    public boolean isExist(Integer drugId) {
        LambdaQueryWrapper<Drug> eq = new LambdaQueryWrapper<Drug>().eq(Drug::getDrugId, drugId);
        return drugsMapper.exists(eq);
    }
}
