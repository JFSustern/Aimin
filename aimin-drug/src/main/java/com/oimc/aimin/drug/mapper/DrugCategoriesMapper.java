package com.oimc.aimin.drug.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.oimc.aimin.drug.entity.DrugCategories;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */

public interface DrugCategoriesMapper extends MPJBaseMapper<DrugCategories> {

    @Select("SELECT * FROM t_drug_categories WHERE category_id = #{categoryId}")
    DrugCategories selectById(Integer categoryId);
}
