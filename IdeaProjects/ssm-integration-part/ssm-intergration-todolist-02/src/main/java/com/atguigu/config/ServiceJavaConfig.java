package com.atguigu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 1. service
 * 2. 开启aop注解的支持 aspect : @Before @After @AfterReturning ...
 * 3. tx声明式事务 1. 对应事务管理器实现 2. 开启事务注解支持 @Transactional
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("com.atguigu.service")
public class ServiceJavaConfig {

    public TransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

}
