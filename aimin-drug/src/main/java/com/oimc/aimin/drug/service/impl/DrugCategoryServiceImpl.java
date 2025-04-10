package com.oimc.aimin.drug.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.drug.mapper.DrugCategoriesMapper;
import com.oimc.aimin.drug.service.DrugCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
@Service
@AllArgsConstructor
public class DrugCategoryServiceImpl extends ServiceImpl<DrugCategoriesMapper, DrugCategory> implements DrugCategoryService {

    private final ApplicationContext context;

    public static final String CACHE_NAME = "drugCategory";

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }
}
