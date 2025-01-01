package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drugs;
import com.oimc.aimin.drug.mapper.DrugsMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DrugsServiceImpl extends ServiceImpl<DrugsMapper, Drugs> implements DrugsService {

    private final DrugsMapper drugsMapper;

    @Override
    public Page<Drugs> selectPage(DrugQueryDTO dto) {
        Page<Drugs> drugsPage = new Page<>(dto.getCurrent(), dto.getSize());
        return drugsMapper.selectDrugKindsPage(drugsPage);
    }
}
