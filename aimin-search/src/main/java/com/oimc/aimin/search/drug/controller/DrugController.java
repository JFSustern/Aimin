package com.oimc.aimin.search.drug.controller;

import com.oimc.aimin.base.resp.PageResp;
import com.oimc.aimin.base.resp.Result;
import com.oimc.aimin.search.drug.model.convertor.DrugConvertor;
import com.oimc.aimin.search.drug.model.index.DrugIndex;
import com.oimc.aimin.search.drug.model.request.DrugPageRequest;
import com.oimc.aimin.search.drug.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/*
 *
 * @author 渣哥
 */

@RestController
@RequestMapping("/search/drug")
@RequiredArgsConstructor
@Tag(name = "药品查询", description = "基于ES的药品查询")
public class DrugController {

    private final DrugService drugService;
    private final DrugConvertor drugConvertor;

    @Operation(summary = "药品分页查询", description = "分页获取药品列表，支持关键字查询")
    @PostMapping
    public Result<?> searchPage(@RequestBody @Valid DrugPageRequest request) {
        PageResp<DrugIndex> pageResp = drugService.pageQuery(request);
        return Result.success(pageResp);
    }

    @Operation(summary = "药品分页高亮查询", description = "分页获取药品列表并高亮展示，支持关键字查询")
    @PostMapping("/light")
    public Result<?> light(@RequestBody @Valid DrugPageRequest request) {
        PageResp<DrugIndex> pageResp = drugService.lightQuery(request);
        return Result.success(pageResp);
    }


}
