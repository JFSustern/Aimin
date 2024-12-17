package com.oimc.aimin.auth.controller;



import com.oimc.aimin.common.core.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class WxController {

    @GetMapping("/")
    public Result<?> login(){
        return Result.success("");
    }

}
