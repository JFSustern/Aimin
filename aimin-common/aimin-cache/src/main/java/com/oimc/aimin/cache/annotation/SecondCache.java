package com.oimc.aimin.cache.annotation;


import com.oimc.aimin.cache.config.CacheType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAspectJAutoProxy
public @interface SecondCache {
    String cacheName() default "aimin-cache";
    String key();	//支持springEl表达式
    long l2TimeOut() default 120;
    CacheType type() default CacheType.FULL;
}
