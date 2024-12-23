package com.oimc.aimin.auth.controller;

import com.oimc.aimin.common.core.pojo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/index")
    public Result<?> index() {
        return Result.success("success");
    }
}
