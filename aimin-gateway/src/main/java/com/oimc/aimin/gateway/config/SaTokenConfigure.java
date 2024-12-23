package com.oimc.aimin.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.oimc.aimin.gateway.config.properties.IgnoreWhiteProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure {
    private static final Logger logger = Logger.getLogger(SaTokenConfigure.class.getName());

    private final IgnoreWhiteProperties ignoreWhite;

    @Bean
    public SaReactorFilter getSaReactorFilter( ) {
        return new SaReactorFilter()
                // 指定 [拦截路由]
                .addInclude("/**")    /* 拦截所有path */
                // 指定 [放行路由]
                .addExclude("/aimin-auth/wx/token")
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch(ignoreWhite.getWhites())
                            .check(r -> {
                                StpUtil.checkLogin();
                            });
                })
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    logger.log(Level.WARNING,"sa全局异常");
                    return SaResult.error(e.getMessage());
                });
    }
}
