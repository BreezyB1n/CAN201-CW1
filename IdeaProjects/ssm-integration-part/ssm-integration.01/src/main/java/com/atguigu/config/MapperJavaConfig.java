package com.atguigu.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 * 持久层配置
 */
@Configuration
public class MapperJavaConfig {
    // 问题：如果将dataSource和mybatis组件配置到一起，会出现@Value注解不生效问题
    // 原因：mybatis组件优先加载，@Value还没有读取
    // 解决：分开配置，写到不同的类即可


    //sqlSessionFactory加到ioc容器
    // 方式一：外部指定mybatis-config.xml文件[mybatis配置文件，除了连接池+mapper指定]
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //指定数据库连接池对象
        sqlSessionFactoryBean.setDataSource(dataSource);
        //指定外部mybatis配置文件
        // spring-core包
        Resource resource = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(resource);

        return sqlSessionFactoryBean;
    }

    //mapper代理对象加入到ioc
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.atguigu.mapper"); //mapper接口和mapper.xml所在的共同包
        return mapperScannerConfigurer;
    }
}
