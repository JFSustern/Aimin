package com.oimc.aimin.mp.config;


import com.oimc.aimin.mp.dict.service.DictServiceImpl;
import com.oimc.aimin.mp.translate.DictTranslateInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;



@AutoConfiguration
@Import({
        MybatisPlusPaginationConfig.class,
        MyMetaObjectHandler.class,
        DictTranslateInterceptor.class,
        DictServiceImpl.class
})
@MapperScan("com.oimc.aimin.mp.**.mapper")
public class MybatisPlusDruidAutoConfiguration {


}
