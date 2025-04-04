package com.oimc.aimin.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.oimc.aimin.admin.mapper")
@EnableCaching
public class AiminAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiminAdminApplication.class, args);
    }
}
