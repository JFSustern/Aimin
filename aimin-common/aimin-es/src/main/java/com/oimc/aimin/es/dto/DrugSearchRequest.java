package com.oimc.aimin.es.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Data
public class DrugSearchRequest implements Serializable {

    private String keyword;       // 搜索关键词
    private int page = 0;             // 页码（从 0 开始）
    private int size = 10;             // 每页大小
    private String sortField;     // 排序字段
    private Sort.Direction direction; // 排序方向
}
