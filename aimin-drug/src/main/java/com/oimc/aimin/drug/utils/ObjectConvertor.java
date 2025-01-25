package com.oimc.aimin.drug.utils;


import com.oimc.aimin.drug.controller.vo.DrugVO;
import com.oimc.aimin.drug.dto.DrugDto;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.es.index.DrugIndex;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    List<DrugIndex> drugList2IndexList(List<Drug> drugList);

    DrugIndex drug2DrugIndex(Drug drug);

    List<DrugVO> drugIndexList2DrugVoList(List<DrugIndex> drugIndices);

    Drug drugDto2Drug(DrugDto dto);
}
