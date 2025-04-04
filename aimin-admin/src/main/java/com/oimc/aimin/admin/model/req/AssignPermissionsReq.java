package com.oimc.aimin.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色分配权限参数")
public class AssignPermissionsReq {

    @Schema(description = "角色id", example = "1")
    @NotBlank(message = "角色ID不能为空")
    private Integer roleId;
    private List<Integer> permissionIds;

}