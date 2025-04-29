package com.oimc.aimin.drug.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.oimc.aimin.base.enums.DrugStatus;
import com.oimc.aimin.ds.annotation.DictTranslate;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 药品表，用于存储具体药品的信息和库存状态
 * </p>
 *
 * @author root
 * @since 2024/12/30
 */
@Data
@TableName("t_drug")
public class Drug implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 药品ID，主键，自增
     */
    @TableId(value = "drug_id", type = IdType.AUTO)
    private Integer drugId;

    /**
     * 药品名称，例如“阿莫西林胶囊”
     */
    @TableField("name")
    private String name;

    /**
     * 药品通用名，药品的标准学名
     */
    @TableField("generic_name")
    private String genericName;

    /**
     * 分类ID，与 t_drug_categories 表关联
     */
    @TableField("category_id")
    private Integer categoryId;

    @TableField(exist = false)
    @FieldMapping(tag= DrugCategory.class, thisField = "categoryId", joinField = "categoryId", select = "name")
    private String categoryName;

    /**
     * 品牌名称，例如“辉瑞”
     */
    @TableField("brand")
    private String brand;

    /**
     * 生产厂家，例如“华东制药”
     */
    @TableField("manufacturer")
    private String manufacturer;

    /**
     * 药品描述，详细说明药品的用途和特点
     */
    @TableField("description")
    private String description;

    /**
     * 剂型，例如“片剂”、“胶囊”、“注射液”
     */
    @TableField("dosage_form")
    private String dosageForm;

    /**
     * 规格，例如“500mg”、“100单位/ml”
     */
    @TableField("strength")
    private String strength;

    /**
     * 售价，单位为人民币元
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 折扣价，可为空
     */
    @TableField("discount_price")
    private BigDecimal discountPrice;

    /**
     * 库存数量
     */
    @TableField("stock_quantity")
    private Integer stockQuantity;

    /**
     * 库存单位编号（SKU），唯一
     */
    @TableField("sku")
    private String sku;

    /**
     * 条形码，唯一
     */
    @TableField("bar_code")
    private String barCode;

    /**
     * 是否需要处方，TRUE 表示需要，FALSE 表示不需要
     */
    @TableField("prescription")
    private Boolean prescription;

    /**
     * 保质期长度
     */
    @TableField("shelf_life")
    private Integer shelfLife;

    /**
     * 保质期单位，对应枚举类
     */
    @TableField("shelf_life_unit")
    @DictTranslate(valueTo = "shelfLifeUnitName")
    private Integer shelfLifeUnit;

    @TableField(exist = false)
    private String shelfLifeUnitName;

    /**
     * 储存说明，例如“避光保存”或“冷藏保存”
     */
    @TableField("storage_instructions")
    private String storageInstructions;

    /**
     * 药品创建时间，默认当前时间
     */
    @TableField("create_at")
    private LocalDateTime createAt;

    /**
     * 药品更新时间，自动更新为当前时间
     */
    @TableField("update_at")
    private LocalDateTime updateAt;

    /**
     * 药品状态
     */
    @TableField("status")
    private DrugStatus status;

    /**
     * 是否逻辑删除，TRUE 表示已删除，FALSE 表示未删除
     */
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    private DrugCategory categories;

    @TableField(exist = false)
    @EntityMapping(thisField = "drugId", joinField = "drugId")
    private List<DrugImg> drugImgList;
}