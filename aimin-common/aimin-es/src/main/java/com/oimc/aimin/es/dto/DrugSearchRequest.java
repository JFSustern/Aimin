package com.oimc.aimin.es.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class DrugSearchRequest {

    private String keyword;       // 搜索关键词
    private int page;             // 页码（从 0 开始）
    private int size;             // 每页大小
    private String sortField;     // 排序字段
    private Sort.Direction direction; // 排序方向
}
