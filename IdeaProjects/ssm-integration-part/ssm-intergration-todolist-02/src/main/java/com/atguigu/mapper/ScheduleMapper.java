package com.atguigu.mapper;

import com.atguigu.pojo.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ScheduleMapper {
    List<Schedule> queryList();

    int deleteById(Integer id);

    int insert(Schedule schedule);

    int update(Schedule schedule);
}

