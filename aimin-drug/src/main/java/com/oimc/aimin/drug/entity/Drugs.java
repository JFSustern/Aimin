package com.oimc.aimin.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@TableName("t_drugs")
public class Drugs implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "drug_id", type = IdType.AUTO)
    private Long drugId;
    @TableField("name")
    private String name;
    @TableField("generic_name")
    private String genericName;
    @TableField("category_id")
    private Long categoryId;
    @TableField("brand")
    private String brand;
    @TableField("manufacturer")
    private String manufacturer;
    @TableField("description")
    private String description;
    @TableField("dosage_form")
    private String dosageForm;
    @TableField("strength")
    private String strength;
    @TableField("price")
    private BigDecimal price;
    @TableField("discount_price")
    private BigDecimal discountPrice;
    @TableField("stock_quantity")
    private Integer stockQuantity;
    @TableField("sku")
    private String sku;
    @TableField("bar_code")
    private String barCode;
    @TableField("prescription_required")
    private Boolean prescriptionRequired;
    @TableField("expiry_date")
    private LocalDate expiryDate;
    @TableField("storage_instructions")
    private String storageInstructions;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    @TableField("status")
    private String status;
    @TableField("is_deleted")
    private Boolean isDeleted;
}