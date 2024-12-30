package com.oimc.aimin.cache.l2cache.annotation;


import com.oimc.aimin.cache.l2cache.config.CacheType;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAspectJAutoProxy
public @interface L2Cache {
    String cacheName() default "aimin-cache";
    String key();	//支持springEl表达式
    long l2TimeOut() default 120;
    CacheType type() default CacheType.FULL;
}
