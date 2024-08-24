package com.atguigu.service;

import com.atguigu.pojo.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {

    List<Employee> findALl();

}
