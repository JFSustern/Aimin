package com.oimc.aimin.search.drug.model.convertor;


import com.oimc.aimin.base.model.drug.index.DrugIndex;
import com.oimc.aimin.search.drug.model.vo.DrugVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DrugConvertor {

    /**
     *
     * @param drugIndices
     * @return
     */
    List<DrugVO> index2VO(List<DrugIndex> drugIndices);


}
