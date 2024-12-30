package com.oimc.aimin.drug.controller;

import com.oimc.aimin.common.core.pojo.Result;
import com.oimc.aimin.drug.dto.DrugQueryDTO;
import com.oimc.aimin.drug.entity.Drugs;
import com.oimc.aimin.drug.service.DrugsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {

    private final DrugsService drugsService;

    @PostMapping("/list")
    public Result<?> list(DrugQueryDTO dto) {
        DrugQueryDTO page = drugsService.page(dto);
        List<Drugs> records = page.getRecords();
        return Result.success(records);
    }
}
