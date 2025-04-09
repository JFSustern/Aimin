package com.oimc.aimin.drug;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.oimc.aimin.drug.**.mapper")
@SpringBootApplication
@EnableCaching
public class AiminDrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiminDrugApplication.class, args);
    }

}
