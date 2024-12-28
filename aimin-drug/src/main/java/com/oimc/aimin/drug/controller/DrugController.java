package com.oimc.aimin.drug.controller;

import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.entity.Drugs;
import com.oimc.aimin.drug.service.DrugsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {

    private DrugsService drugsService;

    @GetMapping("/list")
    public Result<?> token(String code) {
        List<Drugs> list = drugsService.list();
        return Result.success(list);
    }
}
