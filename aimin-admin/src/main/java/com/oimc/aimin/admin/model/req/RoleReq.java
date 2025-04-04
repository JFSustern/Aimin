package com.oimc.aimin.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色查询参数")
public class RoleReq {
    /**
     * 模糊查询内容
     */
    @Schema(description = "模糊查询内容", example = "admin")
    private String content;

}
