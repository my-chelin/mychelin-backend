package com.a206.mychelin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 정적 리소스에 대한 설정
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 정적 리소스 위치 목록
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS={
            "classpath:/static/","class:/public/","classpath:/resources","classpath:/META-INF/resources/webjars"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
