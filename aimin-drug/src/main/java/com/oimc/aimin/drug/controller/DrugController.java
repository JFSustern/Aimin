package com.oimc.aimin.drug.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.controller.vo.DrugVO;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.service.DrugEsOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;
    private final DrugEsOperationService drugEsOperationService;
    private final ObjectConvertor objectConvertor;

    @PostMapping("/list")
    public Result<?> list(@RequestBody DrugQueryDTO dto) {
        Page<Drug> page = drugService.selectPage(dto);
        System.out.println(page.getRecords().size());
        return Result.success(page);
    }

    @PostMapping("/put")
    public Result<?> put(@RequestBody DrugDTO dto){
        List<Drug> drugs = drugService.selectAll();
        List<DrugIndex> drugIndices = objectConvertor.drugListToIndexList(drugs);
        drugEsOperationService.saveAll(drugIndices);
        return Result.success();
    }
    @PostMapping("/queryByKeyWord")
    public  Result<?> queryByKeyWord(DrugQueryDTO dto){
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
