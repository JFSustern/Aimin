package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drugs;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
public interface DrugsService extends IService<Drugs> {

    Page<Drugs> selectPage(DrugQueryDTO dto);
}
