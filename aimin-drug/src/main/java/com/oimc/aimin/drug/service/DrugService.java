package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.cache.formal.service.BaseCacheService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
public interface DrugService extends BaseCacheService<Drug> {

    Page<Drug> pageSearchFuzzy(PageDrugRequest page);

    boolean isExist(Integer drugId);
}
