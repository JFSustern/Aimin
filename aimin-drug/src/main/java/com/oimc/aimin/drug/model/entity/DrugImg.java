package com.oimc.aimin.drug.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.annotation.EntityMapping;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 药品图片实体类
 * </p>
 *
 * @author 渣哥
 * @since 2025/01/15
 */
@Data
@TableName("t_drug_img")
public class DrugImg implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联药品ID
     */
    @TableField("drug_id")
    private Integer drugId;

    /**
     * 图片存储路径
     */
    @TableField("path")
    private String path;

    /**
     * 图片现在名称
     */
    @TableField("current_name")
    private String currentName;

    /**
     * 图片现在名称
     */
    @TableField("original_name")
    private String originalName;

    /**
     * 图片说明
     */
    @TableField("detail")
    private String detail;

    /**
     * 是否主图（0否 1是）
     */
    @TableField("is_main")
    private Boolean isMain;

    /**
     * 排序权重
     */
    @TableField("sort")
    private Byte sort;

    /**
     * 上传时间
     */
    @TableField("upload_at")
    private LocalDateTime uploadAt;

    /**
     * 修改时间
     */
    @TableField("update_at")
    private LocalDateTime updateAt;

    /**
     * 上传人
     */
    @TableField("upload_user")
    private Integer uploadUser;

    /**
     * 文件大小（KB）
     */
    @TableField("file_size")
    private Integer fileSize;

    /**
     * 文件格式
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 关联的药品信息
     */
    @TableField(exist = false)
    @EntityMapping(thisField = "drugId", joinField = "drugId")
    private Drug drug;

}