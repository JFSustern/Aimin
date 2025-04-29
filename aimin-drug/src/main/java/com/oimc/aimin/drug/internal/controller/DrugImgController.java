package com.oimc.aimin.drug.internal.controller;

import com.aliyuncs.exceptions.ClientException;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.base.response.Result;
import com.oimc.aimin.drug.facade.DrugImgFacadeService;
import com.oimc.aimin.drug.model.vo.DrugImgVO;
import com.oimc.aimin.drug.service.DrugImgService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.file.base.FileService;
import com.oimc.aimin.file.oss.OssUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 药品图片控制器
 * @author 渣哥
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/drug/img")
public class DrugImgController {

    private final DrugImgService drugImgService;

    private final DrugImgFacadeService drugImgFacadeService;

    private final ObjectConvertor objectConvertor;

    private final FileService fileService;


    /**
     * 保存药品图片
     *
     * @return 保存结果
     */
    @PostMapping("/batchUpload")
    public Result<?> batchUpload(
            @Parameter(description = "图片文件列表", required = true)
            @RequestParam("files") List<MultipartFile> files)  {

        return drugImgFacadeService.uploadDrugImg(files);
    }

    
    /**
     * 设置药品主图
     *
     * @param imageId 图片ID
     * @return 设置结果
     */
    @PutMapping("/main/{imageId}")
    public Result<Boolean> setMainImage(@PathVariable("imageId") Integer imageId) {
        boolean result = drugImgService.setMainImage(imageId);
        if (!result) {
            return Result.error("设置主图失败");
        }
        return Result.success(true);
    }
    
    /**
     * 获取图片详情
     *
     * @param imageId 图片ID
     * @return 图片信息
     */
    @GetMapping("/{imageId}")
    public Result<?> getImageById(@PathVariable("imageId") Integer imageId) {
        DrugImg drugImg = drugImgService.getById(imageId);
        if (drugImg == null) {
            return Result.error("图片不存在");
        }
        DrugImgVO vo = objectConvertor.toVO(drugImg);
        vo.setExternalPath(fileService.externalPath(drugImg.getPath()));
        return Result.success(vo);
    }
    
    /**
     * 获取药品图片列表
     *
     * @param drugId 药品ID
     * @return 图片列表
     */
    @GetMapping("/list/{drugId}")
    public Result<?> listByDrugId(@PathVariable("drugId") Integer drugId) {
        List<DrugImg> drugImgList = drugImgService.listByDrugId(drugId);
        List<DrugImgVO> drugImgVOS = objectConvertor.toVO(drugImgList);
        drugImgVOS.forEach(drugImg -> {
            drugImg.setExternalPath(fileService.externalPath(drugImg.getPath()));
        });

        return Result.success(drugImgVOS);
    }
    
    /**
     * 删除图片
     *
     * @param imageId 图片ID
     * @return 删除结果
     */
    @DeleteMapping("/{imageId}")
    public Result<Boolean> deleteImage(@PathVariable("imageId") Integer imageId) throws ClientException {
        boolean result = drugImgFacadeService.deleteDrugImage(imageId);
        if (result) {
            return Result.success(true);
        }
        return Result.error("图片删除失败");
    }
} 