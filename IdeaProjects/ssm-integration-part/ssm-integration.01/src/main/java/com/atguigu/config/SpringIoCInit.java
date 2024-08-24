package com.atguigu.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringIoCInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        // root ioc 容器配置类
        return new Class[]{DatasourceJavaConfig.class, MapperJavaConfig.class, ServiceJavaConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // web ioc容器配置类
        return new Class[]{WebMvcJavaConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
