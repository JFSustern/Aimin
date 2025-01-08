package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drug;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
public interface DrugService extends IService<Drug> {

    Page<Drug> selectPage(DrugQueryDTO dto);

    List<Drug> selectAll();
}
