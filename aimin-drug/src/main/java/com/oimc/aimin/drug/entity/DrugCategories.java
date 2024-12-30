package com.oimc.aimin.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_drug_categories")
public class DrugCategories implements Serializable {

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
    private Long parentId;

    /**
     * 分类描述，详细说明该分类的用途或特点
     */
    @TableField("description")
    private String description;

    /**
     * 分类创建时间，默认当前时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 分类更新时间，自动更新为当前时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private List<Drugs> drugsList;

}