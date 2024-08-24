package com.atguigu.controller;

import com.atguigu.pojo.Employee;
import com.atguigu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("emp")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("find")
    public List<Employee> find() {
        List<Employee> aLl = employeeService.findALl();
        return aLl;
    }
}
