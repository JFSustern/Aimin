package com.oimc.aimin.cache.l2cache.aspect;


import com.github.benmanes.caffeine.cache.Cache;
import com.oimc.aimin.cache.l2cache.annotation.L2Cache;
import com.oimc.aimin.cache.l2cache.config.CacheType;
import com.oimc.aimin.cache.l2cache.utils.ElParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class L2CacheAspect {

    public static final String COLON = "::";

    private final Cache<String, Object> cache;
    private final RedisTemplate<Object,Object> redisTemplate;

    @Pointcut("@annotation(com.oimc.aimin.cache.l2cache.annotation.L2Cache)")
    public void cacheAspect() {
    }

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //拼接解析springEl表达式的map
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            treeMap.put(paramNames[i],args[i]);
        }

        L2Cache annotation = method.getAnnotation(L2Cache.class);
        String elResult = ElParser.parse(annotation.key(), treeMap);
        String realKey = annotation.cacheName() + COLON + elResult;

        //强制更新
        if (annotation.type()== CacheType.PUT){
            Object object = point.proceed();
            redisTemplate.opsForValue().set(realKey, object,annotation.l2TimeOut(), TimeUnit.SECONDS);
            cache.put(realKey, object);
            return object;
        }

        //删除
        else if (annotation.type()== CacheType.DELETE){
            redisTemplate.delete(realKey);
            cache.invalidate(realKey);
            return point.proceed();
        }

        //读写，查询Caffeine
        Object caffeineCache = cache.getIfPresent(realKey);
        if (Objects.nonNull(caffeineCache)) {
            log.info("get data from caffeine");
            return caffeineCache;
        }

        //查询Redis
        Object redisCache = redisTemplate.opsForValue().get(realKey);
        if (Objects.nonNull(redisCache)) {
            log.info("get data from redis");
            cache.put(realKey, redisCache);
            return redisCache;
        }

        log.info("get data from database");
        Object object = point.proceed();
        if (Objects.nonNull(object)){
            //写入Redis
            redisTemplate.opsForValue().set(realKey, object,annotation.l2TimeOut(), TimeUnit.SECONDS);
            //写入Caffeine
            cache.put(realKey, object);
        }
        return object;
    }
}
