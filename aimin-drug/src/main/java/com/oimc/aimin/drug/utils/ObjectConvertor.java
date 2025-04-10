package com.oimc.aimin.drug.utils;

import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    Drug toDrug(DrugRequest dto);

    List<Drug> toDrug(List<DrugRequest> dto);

    DrugCategory toDrugCategory(DrugCategoryRequest request);
}
