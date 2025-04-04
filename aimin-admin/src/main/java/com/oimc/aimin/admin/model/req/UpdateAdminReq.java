package com.oimc.aimin.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * 更新管理员请求对象
 */
@Data
@Schema(description = "更新管理员请求")
public class UpdateAdminReq {

    @Schema(description = "管理员ID", required = true)
    @NotNull(message = "管理员ID不能为空")
    private Integer aid;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别")
    private Boolean gender;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "部门ID")
    private Integer deptId;

    @Schema(description = "角色ID列表")
    private List<Integer> roleIds;

    @Schema(description = "描述信息")
    private String description;
} 