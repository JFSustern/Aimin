package com.oimc.aimin.admin.feign;

import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.base.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 *
 * @author 渣哥
 */
@FeignClient(name = "aimin-drug",path = "/aimin-drug")
public interface DrugFeignClient {

    @RequestMapping(value = "/api/drug",method = RequestMethod.POST)
    Result<?> insert(@RequestBody DrugRequest drug);

    @RequestMapping(value = "/api/drug",method = RequestMethod.DELETE)
    Result<?> batchDelete(@RequestBody List<Integer> ids);

    @RequestMapping(value = "/api/drug",method = RequestMethod.PUT)
    Result<?> update(@RequestBody DrugRequest drug);

    @RequestMapping(value = "/api/drug/{id}",method = RequestMethod.GET)
    Result<?> getById(@PathVariable("id") String id);

    @RequestMapping(value = "/api/drug",method = RequestMethod.GET)
    Result<?> drugList();

    @RequestMapping(value = "/api/drug/page",method = RequestMethod.POST)
    Result<?> page(@RequestBody PageDrugRequest page);

    @RequestMapping(value = "/api/drug/category/tree/{id}",method = RequestMethod.GET)
    Result<?> categoryTree(@PathVariable("id") Integer id);

    @RequestMapping(value = "/api/drug/category",method = RequestMethod.GET)
    Result<?> categoryList();

    @RequestMapping(value = "/api/drug/category",method = RequestMethod.POST)
    Result<?> insertCategory(@RequestBody DrugCategoryRequest request);


    /**
     * 保存药品图片
     *
     * @param files 文件list
     * @return 保存结果
     */
    @RequestMapping(value = "/drug/img/batchUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    Result<?> batchUpload(@RequestPart List<MultipartFile> files);


    /**
     * 设置药品主图
     *
     * @param imageId 图片ID
     * @return 设置结果
     */
    @RequestMapping(value = "/drug/img/main/{imageId}", method = RequestMethod.PUT)
    Result<Boolean> setMainImage(@PathVariable("imageId") Integer imageId);

    /**
     * 获取图片详情
     *
     * @param imageId 图片ID
     * @return 图片信息
     */
    @RequestMapping(value = "/drug/img/{imageId}", method = RequestMethod.GET)
    Result<?> getById(@PathVariable("imageId") Integer imageId);

    /**
     * 获取药品图片列表
     *
     * @param drugId 药品ID
     * @return 图片列表
     */
    @RequestMapping(value = "/drug/img/list/{drugId}",method = RequestMethod.GET)
    Result<?> listByDrugId(@PathVariable("drugId") Integer drugId);

    /**
     * 删除图片
     *
     * @param imageId 图片ID
     * @return 删除结果
     */
    @RequestMapping(value = "/drug/img/{imageId}", method = RequestMethod.DELETE)
    Result<Boolean> deleteImage(@PathVariable("imageId") Integer imageId);

    /**
     * 删除图片
     *
     * @param filePath 图片地址
     * @return 删除结果
     */
    @RequestMapping(value = "/drug/img/{path}", method = RequestMethod.DELETE)
    void deleteImageByPath(@PathVariable("path") String filePath);
}
