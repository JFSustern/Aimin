package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.mapper.DrugMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugsMapper;

    @Override
    public Page<Drug> selectPage(DrugQueryDTO dto) {
        Page<Drug> page = new Page<>(dto.getCurrent(), dto.getSize());
        return drugsMapper.selectDrugKindsPage(page);
    }

    @Override
    public List<Drug> selectAll() {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        return drugsMapper.selectList(queryWrapper);
    }
}
