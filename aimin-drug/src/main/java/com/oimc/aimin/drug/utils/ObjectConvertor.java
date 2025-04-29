package com.oimc.aimin.drug.utils;

import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.base.response.FileUploadResult;
import com.oimc.aimin.drug.model.vo.DrugImgVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ObjectConvertor {

    Drug toDrug(DrugRequest request);

    List<Drug> toDrug(List<DrugRequest> dto);

    DrugCategory toDrugCategory(DrugCategoryRequest request);

    DrugImg toDrugImg(FileUploadResult fileResult);

    List<DrugImg> toDrugImg(List<FileUploadResult> fileUploadResults);

    List<DrugImgVO> toVO(List<DrugImg> drugImgList);

    DrugImgVO toVO(DrugImg drugImg);
}
