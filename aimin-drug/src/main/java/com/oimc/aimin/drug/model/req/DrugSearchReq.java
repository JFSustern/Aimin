package com.oimc.aimin.drug.model.req;

import com.oimc.aimin.base.req.BasePageReq;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DrugSearchReq extends BasePageReq {

    private String keyword;         // 搜索关键词

}
