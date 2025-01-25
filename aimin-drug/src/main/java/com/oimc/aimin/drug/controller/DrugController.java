package com.oimc.aimin.drug.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.controller.vo.DrugVO;
import com.oimc.aimin.drug.dto.DrugDto;
import com.oimc.aimin.drug.dto.DrugSearchDto;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.service.DrugEsOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;
    private final DrugEsOperation drugEsOperationService;
    private final ObjectConvertor objectConvertor;

    @PostMapping("/search")
    public Result<?> search(@RequestBody DrugSearchRequest request) {
        List<DrugVO> drugVOList = drugService.searchWithHighlight(request);
        return Result.success(drugVOList);
    }

    @PostMapping("/pageList")
    public Result<?> pageList(@RequestBody @Valid DrugSearchRequest request){
        List<DrugVO> drugVOList = drugService.pageList(request);
        return Result.success(drugVOList);
    }

    @PostMapping("/put")
    public Result<?> put(@RequestBody @Valid DrugDto dto){
        Drug drug = objectConvertor.drugDto2Drug(dto);
        drugService.saveAndSyncToES(drug);
        return Result.success();
    }


    @PostMapping("/queryByKeyWord")
    public Result<?> queryByKeyWord(DrugSearchDto dto){
        DrugSearchRequest request = new DrugSearchRequest();
        request.setKeyword(dto.getKeyword());
        request.setPage(1);
        request.setSize(10);
        request.setDirection(Sort.Direction.ASC);
        List<DrugIndex> drugIndices = drugEsOperationService.searchDrugsWithHighlight(request);
        List<DrugVO> voList = objectConvertor.drugIndexList2DrugVoList(drugIndices);
        return Result.success(voList);
    }
}
