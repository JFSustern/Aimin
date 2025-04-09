package com.oimc.aimin.admin.controller.drug;

import com.oimc.aimin.admin.feign.DrugFeignClient;
import com.oimc.aimin.base.req.drug.DrugReq;
import com.oimc.aimin.base.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public Result<?> createDrug(@RequestBody DrugReq drug) {
        return drugFeignClient.insert(drug);
    }
    
    /**
     * 批量删除药品
     * 
     * @param ids 药品ID列表
     * @return 操作结果
     */
    @Operation(summary = "批量删除药品", description = "根据ID集合批量删除药品记录")
    @DeleteMapping
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
    public Result<?> updateDrugs(@RequestBody List<DrugReq> drugs) {
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
        return drugFeignClient.list();
    }
}
