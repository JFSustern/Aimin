package com.oimc.aimin.auth.controller;

import com.oimc.aimin.auth.service.impl.TestService;
import com.oimc.aimin.common.core.pojo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final RedisTemplate redisTemplate;

    @GetMapping("/index")
    public Result<?> index() {
        Map<String, String> userById = testService.getUserById(1L);
        return Result.success(userById);
    }
}
