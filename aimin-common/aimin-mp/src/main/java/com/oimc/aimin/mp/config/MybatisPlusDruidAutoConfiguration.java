package com.oimc.aimin.mp.config;


import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.oimc.aimin.mp.dict.service.DictServiceImpl;
import com.oimc.aimin.mp.translate.DictTranslateInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;


@AutoConfiguration
@ConditionalOnClass({
        DataSourceAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class,
        MybatisPlusAutoConfiguration.class
})

@Import({
        MybatisPlusPaginationConfig.class,
        MyMetaObjectHandler.class,
        DictTranslateInterceptor.class,
        DictServiceImpl.class
})
@MapperScan("com.oimc.aimin.mp.**.mapper")
public class MybatisPlusDruidAutoConfiguration {

}
