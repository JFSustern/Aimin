package com.oimc.aimin.admin.controller.drug;

import com.oimc.aimin.admin.feign.DrugFeignClient;
import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.base.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *
 * @author 渣哥
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/drug")
@Tag(name = "药品管理", description = "提供药品的增删改查功能")
public class DrugController {

    private final DrugFeignClient drugFeignClient;

    /**
     * 创建药品
     *
     * @param drug 药品信息请求
     * @return 操作结果
     */
    @Operation(summary = "创建药品", description = "添加新的药品记录")
    @PostMapping
    public Result<?> createDrug(@RequestBody @Validated DrugRequest drug) {
        return drugFeignClient.insert(drug);
    }

    /**
     * 批量删除药品
     *
     * @param ids 药品ID列表
     * @return 操作结果
     */
    @Operation(summary = "批量删除药品", description = "根据ID集合批量删除药品记录")
    @PostMapping("/batchDelete")
    public Result<?> batchDeleteDrugs(@RequestBody List<Integer> ids) {
        return drugFeignClient.batchDelete(ids);
    }

    /**
     * 更新药品信息
     *
     * @param drugs 药品更新信息列表
     * @return 操作结果
     */
    @Operation(summary = "更新药品", description = "批量更新指定药品的信息")
    @PutMapping
    public Result<?> updateDrugs(@RequestBody DrugRequest drugs) {
        return drugFeignClient.update(drugs);
    }

    /**
     * 获取药品详情
     *
     * @param id 药品ID
     * @return 药品详情
     */
    @Operation(summary = "获取药品详情", description = "根据ID获取单个药品的详细信息")
    @GetMapping("/{id}")
    public Result<?> getDrugById(@PathVariable("id") String id) {
        return drugFeignClient.getById(id);
    }

    /**
     * 获取所有药品列表
     *
     * @return 药品列表
     */
    @Operation(summary = "获取药品列表", description = "获取所有药品的列表")
    @GetMapping("/list")
    public Result<?> getAllDrugs() {
        return drugFeignClient.drugList();
    }

    /**
     * 分页查询药品
     *
     * @param page 查询参数，包含pageNum、pageSize和可选的筛选条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询药品", description = "根据条件分页获取药品列表")
    @PostMapping("/page")
    public Result<?> pageDrugs(@RequestBody PageDrugRequest page) {
        return drugFeignClient.page(page);
    }

    @Operation(summary = "药品分类树", description = "药品分类树")
    @GetMapping("/category/tree")
    public Result<?> categoryTree() {
        return drugFeignClient.categoryTree(0);
    }

    @Operation(summary = "药品分类列表", description = "药品分类列表")
    @GetMapping("/category")
    public Result<?> category() {
        return drugFeignClient.categoryList();
    }

    @Operation(summary = "添加药品分类", description = "药品分类列表")
    @PostMapping("/category")
    public Result<?> insertCategory(@RequestBody DrugCategoryRequest request) {
        return drugFeignClient.insertCategory(request);
    }
}