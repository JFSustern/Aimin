package com.oimc.aimin.drug;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.oimc.aimin.drug.**.mapper")
@SpringBootApplication
public class AiminDrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiminDrugApplication.class, args);
    }

}
