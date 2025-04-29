package com.oimc.aimin.drug.internal.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.drug.model.entity.Drug;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.base.response.PageResp;
import com.oimc.aimin.base.response.Result;
import com.oimc.aimin.drug.facade.DrugFacadeService;
import com.oimc.aimin.drug.model.vo.DrugVO;
import com.oimc.aimin.drug.service.DrugCategoryService;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 药品管理控制器
 * 提供药品的增删改查功能
 */
@RestController
@RequestMapping("/api/drug")
@RequiredArgsConstructor
@Tag(name = "药品管理", description = "药品的创建、查询、更新和删除")
public class DrugController {

    private final DrugService drugService;

    private final DrugFacadeService drugFacadeService;

    private final DrugCategoryService drugCategoryService;

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
    public Result<?> insert(@RequestBody @Valid DrugRequest drug) {
        drugFacadeService.insertDrug(drug);
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
    public Result<?> batchDelete(@RequestBody List<Integer> ids) {
        drugService.cacheDelete(ids);
        return Result.success();
    }

    /**
     * 更新药品信息
     * 根据ID更新药品的信息
     *
     * @param request 药品更新信息
     * @return 操作结果
     */
    @Operation(summary = "更新药品", description = "更新指定ID的药品信息")
    @PutMapping
    public Result<?> batchUpdate(@RequestBody @Valid DrugRequest request) {
        drugFacadeService.updateDrug(request);
        return Result.success();
    }

    @Operation(summary = "查询药品", description = "根据ID查询药品信息")
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        Drug drug = drugService.cacheGetById(id);
        return Result.success(drug);
    }

    @Operation(summary = "查询全部药品", description = "查询全部药品")
    @GetMapping
    public Result<?> getByIds() {
        List<Drug> drugs = drugService.cacheGetAll();
        return Result.success(drugs);
    }

    @Operation(summary = "分页查询药品", description = "分页获取药品列表")
    @PostMapping("/page")
    public Result<?> pageDrugs(@RequestBody PageDrugRequest page) {
        Page<Drug> drugPage = drugService.pageSearchFuzzy(page);
        PageResp<DrugVO> drugVOPageResp = PageResp.build(drugPage, DrugVO.class);
        return Result.success(drugVOPageResp);
    }

    @Operation(summary = "获取药品类别树", description = "获取药品分类的树形结构，可指定根节点ID")
    @GetMapping("/category/tree/{id}")
    public Result<List<Tree<Integer>>> tree(@PathVariable Integer id){
        List<Tree<Integer>> tree = drugFacadeService.getCategoryTree(id);
        return Result.success(tree);
    }

    @Operation(summary = "查询全部药品分类", description = "查询全部药品分类")
    @GetMapping("/category")
    public Result<?> categoryList() {
        List<DrugCategory> drugCategories = drugCategoryService.cacheGetAll();
        return Result.success(drugCategories);
    }

    @Operation(summary = "查询全部药品分类", description = "查询全部药品分类")
    @PostMapping("/category")
    public Result<?> insertCategory(@RequestBody DrugCategoryRequest request) {
        DrugCategory drugCategory = objectConvertor.toDrugCategory(request);
        drugCategoryService.cacheInsert(drugCategory);
        return Result.success(drugCategory.getCategoryId());
    }

}
