package com.oimc.aimin.drug.exam.controller;

import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final DrugService drugService;

    @GetMapping("selectAll")
    public Result<?> selectAll(){
        List<Drug> drugs = drugService.selectAll();
        return Result.success(drugs);
    }

    @GetMapping("syncAllData2Es")
    public Result<?> syncAllData2Es(){
        drugService.syncAllData2Es();
        return Result.success();
    }

}
