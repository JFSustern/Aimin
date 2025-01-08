package com.oimc.aimin.drug.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.drug.entity.Drug;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DrugQueryDTO extends Page<Drug> {

    private String keyword;

}
