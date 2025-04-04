package com.oimc.aimin.base.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/*
 *
 * @author 渣哥
 */
@Data
public class BasePageReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer currentPage;
    private Integer pageSize;
}
