package com.oimc.aimin.drug.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.oimc.aimin.drug.model.entity.Drug;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */

public interface DrugMapper extends MPJBaseMapper<Drug> {

    @Select("SELECT " +
            "d.drug_id, \n" +
            "d.name, \n" +
            "d.generic_name, \n" +
            "d.category_id, \n" +
            "d.brand, \n" +
            "d.manufacturer, \n" +
            "d.description, \n" +
            "d.dosage_form, \n" +
            "d.strength, \n" +
            "d.price, \n" +
            "d.discount_price, \n" +
            "d.stock_quantity, \n" +
            "d.sku, \n" +
            "d.bar_code, \n" +
            "d.prescription_required, \n" +
            "d.shelf_life, \n" +
            "d.shelf_life_unit, \n" +
            "d.storage_instructions, \n" +
            "d.created_at, \n" +
            "d.updated_at, \n" +
            "d.status, \n" +
            "d.is_deleted, " +
            "c.category_id, \n" +
            "c.name, \n" +
            "c.parent_id, \n" +
            "c.description, \n" +
            "c.created_at, \n" +
            "c.updated_at \n" +
            "FROM t_drugs d \n" +
            "LEFT JOIN t_drug_categories c ON d.category_id = c.category_id ")
    @Results({
            @Result(property = "drugId", column = "drug_id", id = true),
            @Result(property = "categories", column = "category_id",
                    one = @One(select = "com.oimc.aimin.drug.mapper.DrugCategoriesMapper.selectById")),
    })
    Page<Drug> selectDrugKindsPage(Page<Drug> drugsPage);
}
