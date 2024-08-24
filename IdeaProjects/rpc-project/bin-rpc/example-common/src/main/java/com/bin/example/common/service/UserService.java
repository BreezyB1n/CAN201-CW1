package com.bin.example.common.service;

import com.bin.example.common.model.User;

/**
 * @author shaobin.qin
 * 用户服务
 */
public interface UserService {

    /**
     * 获取对象
     * @param user 对象请求参数
     * @return 对象响应
     */
    User getUser(User user);
}
