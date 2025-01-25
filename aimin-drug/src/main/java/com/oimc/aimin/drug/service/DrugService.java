package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.drug.controller.vo.DrugVO;
import com.oimc.aimin.drug.dto.DrugSearchDto;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.es.dto.DrugSearchRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
public interface DrugService extends MPJDeepService<Drug> {

    List<DrugVO> pageList(DrugSearchRequest request);

    void saveAndSyncToES(Drug drug);

    List<DrugVO> searchWithHighlight(DrugSearchRequest request);

    List<Drug> selectAll();

    void syncAllData2Es();
}
