package com.oimc.aimin.search.drug.model.req;

import com.oimc.aimin.base.req.BasePageReq;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DrugPageReq extends BasePageReq {

    private String keyword;       // 搜索关键词
    private String sortField;     // 排序字段

}
