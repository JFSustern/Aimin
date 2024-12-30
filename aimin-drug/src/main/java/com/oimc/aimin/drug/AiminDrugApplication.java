package com.oimc.aimin.drug;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.oimc.aimin.drug.**.mapper")
@SpringBootApplication
public class AiminDrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiminDrugApplication.class, args);
    }

}
