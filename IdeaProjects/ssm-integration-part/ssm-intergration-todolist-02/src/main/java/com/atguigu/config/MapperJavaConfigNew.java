package com.atguigu.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 不保留外部配置文件，全部mybatis的属性都在代码中设置
 *
 *
 */
@Configuration
@ComponentScan("com.atguigu.mapper")
public class MapperJavaConfigNew {
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

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        //指定mybatis配置的文件的功能，使用代码形式
        sqlSessionFactoryBean.setConfiguration(configuration);

        //别名
        sqlSessionFactoryBean.setTypeAliasesPackage("com.atguigu.pojo");
        //插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect","mysql");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.addPlugins(pageInterceptor);
        //指定外部mybatis配置文件
        // spring-core包
//        Resource resource = new ClassPathResource("mybatis-config.xml");
//        sqlSessionFactoryBean.setConfigLocation(resource);

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
