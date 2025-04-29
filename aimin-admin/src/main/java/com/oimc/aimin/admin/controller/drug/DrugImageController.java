package com.oimc.aimin.admin.controller.drug;

import com.oimc.aimin.admin.feign.DrugFeignClient;
import com.oimc.aimin.base.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 药品图片管理控制器
 * @author 渣哥
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/drug/img")
@Tag(name = "药品图片管理", description = "提供药品图片的上传、保存和管理功能")
public class DrugImageController {

    private final DrugFeignClient feignClient;

    /**
     * 批量上传并保存药品图片
     *
     * @param files 图片文件列表
     * @return 保存结果
     */
    @Operation(summary = "批量上传并保存药品图片", description = "批量上传药品图片并关联到指定药品")
    @PostMapping("/batchUpload")
    public Result<?> batchUploadAndSaveImages(
            @Parameter(description = "图片文件列表", required = true) 
            @RequestParam("files") List<MultipartFile> files) {

        return feignClient.batchUpload(files);
    }

    @Operation(summary = "获取药品图片", description = "根据药品ID获取药品的图片列表")
    @GetMapping("/{drugId}")
    public Result<?> listByDrugId(
            @Parameter(description = "药品ID", required = true)
            @PathVariable("drugId") Integer drugId) {

        return feignClient.listByDrugId(drugId);
    }

    @DeleteMapping("/{imageId}")
    public Result<?> deleteImage(@PathVariable("imageId") Integer imageId){
        return feignClient.deleteImage(imageId);
    }

    /**
     * 设置药品主图
     *
     * @param imageId 图片ID
     * @return 操作结果
     */
    @Operation(summary = "设置药品主图", description = "将指定图片设置为药品的主图")
    @PutMapping("/main/{imageId}")
    public Result<Boolean> setMainImage(
            @Parameter(description = "图片ID", required = true) 
            @PathVariable("imageId") Integer imageId) {
        
        return feignClient.setMainImage(imageId);
    }

}