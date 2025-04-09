package com.oimc.aimin.cache.formal.util;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


/*
 *@title KeyGen
 *@description
 *@author WIN10
 */
public class KeyGen implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String methodName = method.getName();
        String delimitedString = StringUtils.arrayToDelimitedString(params, "_");
        String md5Hex16 = DigestUtil.md5Hex16(delimitedString);
        return STR."\{methodName}:\{md5Hex16}";
    }

}
