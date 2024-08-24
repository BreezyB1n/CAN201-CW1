package com.atguigu.mapper;

import com.atguigu.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

public interface EmployeeMapper {

    List<Employee> queryList();
}
