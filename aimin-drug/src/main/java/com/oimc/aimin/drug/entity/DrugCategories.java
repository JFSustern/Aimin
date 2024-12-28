package com.oimc.aimin.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
@Data
@TableName("t_drug_categories")
public class DrugCategories implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;
    @TableField("name")
    private String name;
    @TableField("parent_id")
    private Long parentId;
    @TableField("description")
    private String description;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}