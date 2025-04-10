package com.oimc.aimin.admin.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema( description ="模糊查询分页参数")
public class PageRequest {
    @Schema(description = "模糊查询内容", example = "admin")
    private String content;

    @Schema(description = "部门id", example = "1")
    private Integer deptId;

    @Schema(description = "分页大小", example = "10")
    private Integer pageSize;

    @Schema(description = "分页索引", example = "1")
    private Integer currentPage;
}
