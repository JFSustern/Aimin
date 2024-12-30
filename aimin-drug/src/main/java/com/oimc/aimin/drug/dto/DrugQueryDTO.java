package com.oimc.aimin.drug.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.drug.entity.Drugs;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DrugQueryDTO extends Page<Drugs> {

    private String keyword;

}
