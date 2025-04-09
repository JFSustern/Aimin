package com.oimc.aimin.drug.service;

import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.drug.mapper.DrugImgMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class DrugImgServiceImpl extends ServiceImpl<DrugImgMapper, DrugImg> implements DrugImgService {

    @Override
    public String getCacheName() {
        return "";
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return null;
    }
}
