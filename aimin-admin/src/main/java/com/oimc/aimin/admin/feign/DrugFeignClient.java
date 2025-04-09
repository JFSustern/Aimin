package com.oimc.aimin.admin.feign;

import com.oimc.aimin.base.req.drug.DrugReq;
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
    Result<?> insert(@RequestBody DrugReq drug);

    @RequestMapping(value = "/api/drug",method = RequestMethod.DELETE)
    Result<?> batchDelete(@RequestBody List<Integer> ids);

    @RequestMapping(value = "/api/drug",method = RequestMethod.PUT)
    Result<?> update(@RequestBody List<DrugReq> drug);

    @RequestMapping(value = "/api/drug/{id}",method = RequestMethod.GET)
    Result<?> getById(@PathVariable("id") String id);

    @RequestMapping(value = "/api/drug/list",method = RequestMethod.GET)
    Result<?> list();



}
