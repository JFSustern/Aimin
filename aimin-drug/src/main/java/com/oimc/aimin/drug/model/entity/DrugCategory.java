package com.oimc.aimin.drug.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * 药品分类表，用于存储药品的分类信息，支持多级分类
 * </p>
 *
 * @author root
 * @since 2024/12/29
 */
@Data
@TableName("t_drug_category")
public class DrugCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID，主键，自增
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 分类名称，例如“抗生素”、“维生素”
     */
    @TableField("name")
    private String name;

    /**
     * 父级分类ID，用于支持多级分类
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 分类描述，详细说明该分类的用途或特点
     */
    @TableField("description")
    private String description;

    /**
     * 分类创建时间，默认当前时间
     */
    @TableField("create_at")
    private LocalDateTime createAt;

    /**
     * 分类更新时间，自动更新为当前时间
     */
    @TableField("update_at")
    private LocalDateTime updateAt;

    @TableField(exist = false)
    private List<Drug> drugsList;

    @TableLogic
    private Boolean deleted;

}