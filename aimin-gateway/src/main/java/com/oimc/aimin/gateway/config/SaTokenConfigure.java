package com.oimc.aimin.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.util.SaResult;
import com.oimc.aimin.gateway.auth.StrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class SaTokenConfigure {
    private static final Logger logger = Logger.getLogger(SaTokenConfigure.class.getName());



    @Bean
    public SaReactorFilter getSaReactorFilter() {

        return new SaReactorFilter()
                .addExclude("/aimin-admin/public/**")
                // 指定 [拦截路由]
                .addInclude("/**")
                .setAuth(obj -> {
                    String path = SaHolder.getRequest().getRequestPath();
                    StrategyFactory.getStrategy(path).checkAuth();
                })
                .setError(e -> {
                    logger.log(Level.WARNING,"sa全局异常",e);
                    return SaResult.error(e.getMessage());
                }).setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> {})
                            .back();
                });
    }
}
