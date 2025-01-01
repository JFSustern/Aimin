package com.oimc.aimin.drug.controller.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oimc.aimin.drug.Enums.DrugStatus;
import com.oimc.aimin.mp.annotation.DictTranslate;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DrugsVO {

    private static final long serialVersionUID = 1L;

    /**
     * 药品ID，主键，自增
     */
    
    private Integer drugId;

    /**
     * 药品名称，例如“阿莫西林胶囊”
     */
    
    private String name;

    /**
     * 药品通用名，药品的标准学名
     */
    
    private String genericName;

    /**
     * 分类ID，与 t_drug_categories 表关联
     */
    
    private Long categoryId;

    /**
     * 品牌名称，例如“辉瑞”
     */
    
    private String brand;

    /**
     * 生产厂家，例如“华东制药”
     */
    
    private String manufacturer;

    /**
     * 药品描述，详细说明药品的用途和特点
     */
    
    private String description;

    /**
     * 剂型，例如“片剂”、“胶囊”、“注射液”
     */
    
    private String dosageForm;

    /**
     * 规格，例如“500mg”、“100单位/ml”
     */
    
    private String strength;

    /**
     * 售价，单位为人民币元
     */
    
    private BigDecimal price;

    /**
     * 折扣价，可为空
     */
    
    private BigDecimal discountPrice;

    /**
     * 库存数量
     */
    
    private Integer stockQuantity;

    /**
     * 库存单位编号（SKU），唯一
     */
    
    private String sku;

    /**
     * 条形码，唯一
     */
    
    private String barCode;

    /**
     * 是否需要处方，TRUE 表示需要，FALSE 表示不需要
     */
    
    private Boolean prescriptionRequired;

    /**
     * 保质期长度
     */
    
    private Integer shelfLife;

    /**
     * 保质期单位，对应枚举类
     */
    
    @DictTranslate(valueTo = "shelfLifeUnitName")
    private Integer shelfLifeUnit;

    @TableField(exist = false)
    private String shelfLifeUnitName;

    /**
     * 储存说明，例如“避光保存”或“冷藏保存”
     */
    
    private String storageInstructions;

    /**
     * 药品创建时间，默认当前时间
     */
    
    private LocalDateTime createdAt;

    /**
     * 药品更新时间，自动更新为当前时间
     */
    
    private LocalDateTime updatedAt;

    /**
     * 药品状态，“Available”表示上架，“Unavailable”表示下架
     */
    
    private DrugStatus status;

    /**
     * 是否逻辑删除，TRUE 表示已删除，FALSE 表示未删除
     */
    
    private Boolean isDeleted;

}
