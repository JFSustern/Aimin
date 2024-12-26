package com.oimc.aimin.auth.service.impl;

import com.oimc.aimin.cache.annotation.SecondCache;
import com.oimc.aimin.cache.config.CacheType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    @SecondCache( cacheName = "caffeineCache", key = "#id", type = CacheType.FULL)
    public Map<String,String> getUserById(Long id) {
        System.out.println("Fetching user from database for ID: " + id);
        return buildUser(String.valueOf(id));
    }


    public Map<String,String> buildUser(String id) {
        Map<String,String> user = new HashMap<>();
        user.put("id",id);
        user.put("name","张三");
        return user;
    }


}
