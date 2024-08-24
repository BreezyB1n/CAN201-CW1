package com.atguigu.service.impl;

import com.atguigu.mapper.EmployeeMapper;
import com.atguigu.pojo.Employee;
import com.atguigu.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> findALl() {
        return employeeMapper.queryList();
    }
}
