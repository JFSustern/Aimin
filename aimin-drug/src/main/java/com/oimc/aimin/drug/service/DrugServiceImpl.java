package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.mapper.DrugMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugsMapper;

    private final ApplicationContext context;


    @Override
    public String getCacheName() {
        return "drug";
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }
}
