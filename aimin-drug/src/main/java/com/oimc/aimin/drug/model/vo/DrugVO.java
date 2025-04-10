package com.oimc.aimin.drug.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DrugVO implements Serializable {

    @Serial
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
    
    private Integer categoryId;

    private String categoryName;


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
     * 是否需要处方，TRUE 表示需要，FALSE 表示不需要
     */
    
    private Boolean prescription;

    /**
     * 保质期长度
     */
    
    private Integer shelfLife;

    /**
     * 保质期单位，对应枚举类
     */

    private Integer shelfLifeUnit;


    private String shelfLifeUnitName;

    /**
     * 储存说明，例如“避光保存”或“冷藏保存”
     */
    
    private String storageInstructions;

}
