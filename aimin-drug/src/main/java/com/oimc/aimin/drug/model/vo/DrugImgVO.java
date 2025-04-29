package com.oimc.aimin.drug.model.vo;

import lombok.Data;

/*
 *
 * @author 渣哥
 */
@Data
public class DrugImgVO {
    private Integer id;
    private Integer drugId;
    private String path;
    private String currentName;
    private String originalName;
    private Boolean isMain;
    private Integer sort;
    private String externalPath;
}
