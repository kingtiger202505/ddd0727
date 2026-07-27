package com.quotation.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验登录状态
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 指定一条 match 规则，Sa-Token 会拦截这个规则里的请求，并校验其登录状态
            StpUtil.checkLogin();
        })).addPathPatterns("/**")
          .excludePathPatterns("/api/quotation/login", "/api/quotation/register");
    }
}
