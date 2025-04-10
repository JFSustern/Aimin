package com.oimc.aimin.admin.feign;

import com.oimc.aimin.base.request.drug.DrugCategoryRequest;
import com.oimc.aimin.base.request.drug.DrugRequest;
import com.oimc.aimin.base.request.drug.PageDrugRequest;
import com.oimc.aimin.base.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    Result<?> update(@RequestBody List<DrugRequest> drug);

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
}
