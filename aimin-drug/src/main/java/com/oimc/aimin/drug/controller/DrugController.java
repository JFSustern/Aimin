package com.oimc.aimin.drug.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drugs;
import com.oimc.aimin.drug.service.DrugsService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugsService drugsService;

    @PostMapping("/list")
    public Result<?> list(@RequestBody DrugQueryDTO dto) {
        Page<Drugs> page = drugsService.selectPage(dto);
        System.out.println(page.getRecords().size());
        return Result.success(page);
    }

}
