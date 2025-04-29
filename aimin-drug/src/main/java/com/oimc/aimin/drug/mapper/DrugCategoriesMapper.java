package com.oimc.aimin.drug.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */

public interface DrugCategoriesMapper extends MPJBaseMapper<DrugCategory> {

    @Select("SELECT * FROM t_drug_category WHERE category_id = #{categoryId}")
    DrugCategory selectById(Integer categoryId);
}
