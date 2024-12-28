package com.oimc.aimin.cache.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;


//开启缓存功能
@EnableCaching
public class CacheConfig {

    @Bean
    public Cache<String,Object> caffeineCache(){
        return Caffeine.newBuilder()
                .initialCapacity(128)//初始大小
                .maximumSize(1024)//最大数量
                .expireAfterWrite(20, TimeUnit.SECONDS)//过期时间
                .build();
    }
}
