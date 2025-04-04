package com.oimc.aimin.drug.service;

import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.drug.model.entity.Drug;
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

    List<Drug> findDrugsJoinCategories();

    void insert(Drug entity);

    void updateByPrimaryKey(Drug entity);
}
