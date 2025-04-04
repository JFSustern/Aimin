package com.oimc.aimin.drug.controller;

import com.oimc.aimin.base.resp.Result;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.req.DrugReq;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 药品管理控制器
 * 提供药品的增删改查功能
 */
@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
@Tag(name = "药品管理", description = "药品的创建、查询、更新和删除")
public class DrugController {

    private final DrugService drugService;
    private final ObjectConvertor objectConvertor;



    /**
     * 创建药品
     * 添加新的药品记录
     *
     * @param drug 药品信息请求对象
     * @return 创建的药品ID
     */
    @Operation(summary = "新增药品", description = "添加新的药品记录")
    @PostMapping
    public Result<?> insert(@RequestBody @Valid DrugReq drug) {
        Drug entity = objectConvertor.toDrug(drug);
        drugService.insert(entity);
        return Result.success(entity.getDrugId());
    }

    /**
     * 更新药品信息
     * 根据ID更新药品的信息
     *
     * @param drug 药品更新信息
     * @return 操作结果
     */
    @Operation(summary = "更新药品", description = "更新指定ID的药品信息")
    @PutMapping
    public Result<?> update(@PathVariable( @RequestBody @Valid List<DrugReq> drug) {
        objectConvertor.toDrugList(drug);

        drugService.updateByPrimaryKey(entity);
        return Result.success();
    }

    /**
     * 批量删除药品
     * 根据ID集合批量删除药品记录
     *
     * @param ids 药品ID集合
     * @return 操作结果
     */
    @Operation(summary = "批量删除药品", description = "根据ID集合批量删除药品记录")
    @DeleteMapping
    public Result<?> batchDelete(@RequestBody Set<Long> ids) {
        drugService.removeByIds(ids);
        return Result.success();
    }

}
