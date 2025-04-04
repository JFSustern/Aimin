package com.oimc.aimin.drug.utils;

import com.oimc.aimin.drug.model.req.DrugReq;
import com.oimc.aimin.drug.model.entity.Drug;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    Drug toDrug(DrugReq dto);

    List<Drug> toDrugList(List<DrugReq> dto);
}
