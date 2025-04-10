package com.oimc.aimin.search.drug.model.request;

import com.oimc.aimin.base.request.BasePageRequest;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DrugPageRequest extends BasePageRequest {

    private String keyword;       // 搜索关键词
    private String sortField;     // 排序字段

}
