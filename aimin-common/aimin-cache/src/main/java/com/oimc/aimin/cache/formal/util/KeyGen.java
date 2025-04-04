package com.oimc.aimin.cache.formal.util;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;


/*
 *@title KeyGen
 *@description
 *@author WIN10
 */
public class KeyGen implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        String className = target.getClass().getSimpleName();
        String methodName = method.getName();
        String paramsHex = DigestUtil.md5Hex(JSONUtil.toJsonStr(params));
        return STR."\{className}:\{methodName}:\{paramsHex}";
    }
}
