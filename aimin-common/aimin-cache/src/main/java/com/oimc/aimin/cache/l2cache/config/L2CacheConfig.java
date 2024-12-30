package com.oimc.aimin.cache.l2cache.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;



//开启缓存功能
@EnableCaching
@RequiredArgsConstructor
public class L2CacheConfig {

    private final L2CacheProperties l2CacheProperties;

    @Bean
    public Cache<String,Object> caffeineCache(){
        return Caffeine.newBuilder()
                .initialCapacity(l2CacheProperties.getInitialCapacity())//初始大小
                .maximumSize(l2CacheProperties.getMaximumSize())//最大数量
                .expireAfterWrite(l2CacheProperties.getExpireAfterWrite(), l2CacheProperties.getExpireAfterWriteTimeUnit())//过期时间
                .build();
    }
}
