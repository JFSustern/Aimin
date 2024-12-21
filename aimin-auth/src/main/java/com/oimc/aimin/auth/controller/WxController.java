package com.oimc.aimin.auth.controller;

import com.oimc.aimin.auth.pojo.Jscode2sessionResult;
import com.oimc.aimin.auth.service.WxService;
import com.oimc.aimin.common.core.pojo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
@RequiredArgsConstructor
public class WxController {

    private final WxService wxService;

    @GetMapping("/token")
    public Result<?> token(String code) {
        Jscode2sessionResult result = wxService.Code2Session(code);

        return Result.success("");
    }


}
