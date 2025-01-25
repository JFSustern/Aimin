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
 * @since 2025/01/15
 */
@Data
@TableName("t_drug_img")
public class DrugImg implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("url")
    private String url;
    @TableField("drug_id")
    private Integer drugId;
    @TableField("order")
    private Integer order;
    @TableField("size")
    private Integer size;
    @TableField("type")
    private Integer type;
    @TableField("is_main")
    private Byte isMain;
    @TableField("create_at")
    private LocalDateTime createAt;
    @TableField("update_at")
    private LocalDateTime updateAt;
}