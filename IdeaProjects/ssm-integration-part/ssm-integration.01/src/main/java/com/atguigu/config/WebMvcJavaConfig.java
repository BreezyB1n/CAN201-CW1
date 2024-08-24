package com.atguigu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 1. controller
 * 2. 全局异常处理器
 * 3. handlerMapping handlerAdapter
 * 4. 静态资源处理
 * 5. jsp
 * 6. json转化器
 * 7. 拦截器
 */
@Configuration
@ComponentScan({"com.atguigu.controller"})
@EnableWebMvc // 解决 3 + 6
public class WebMvcJavaConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 静态资源 解决4
        configurer.enable();
    }

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        // 解决5
//        registry.jsp("/WEB-INF/views/", "jsp");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 解决7
        //registry.addInterceptor().addPathPatterns().excludePathPatterns();
    }
}
