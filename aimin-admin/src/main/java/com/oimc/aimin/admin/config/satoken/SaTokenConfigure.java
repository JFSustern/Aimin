package com.oimc.aimin.admin.config.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.oimc.aimin.satoken.admin.StpAdminUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 渣哥
 */



@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {

        })).excludePathPatterns("/**");
    }
}
