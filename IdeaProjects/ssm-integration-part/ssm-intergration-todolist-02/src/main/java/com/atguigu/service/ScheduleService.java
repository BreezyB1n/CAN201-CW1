package com.atguigu.service;

import com.atguigu.pojo.Schedule;
import com.atguigu.utils.R;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {
    R page(int pageSize, int currentPage);

    R remove(Integer id);

    R save(Schedule schedule);

    R update(Schedule schedule);
}

