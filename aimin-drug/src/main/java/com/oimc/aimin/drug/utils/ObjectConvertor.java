package com.oimc.aimin.drug.utils;


import com.oimc.aimin.drug.entity.Drugs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    // List<DrugsVO>drugListToVoList(List<Drugs> drugList);
}
