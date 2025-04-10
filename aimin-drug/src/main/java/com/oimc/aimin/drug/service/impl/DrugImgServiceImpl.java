package com.oimc.aimin.drug.service.impl;

import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.drug.mapper.DrugImgMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.service.DrugImgService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2025/01/15
 */
@Service
@AllArgsConstructor
public class DrugImgServiceImpl extends ServiceImpl<DrugImgMapper, DrugImg> implements DrugImgService {

    private final ApplicationContext context;

    public static final String CACHE_NAME = "drugImg";

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }
}
