package com.bin.example.provider;

import com.bin.example.common.model.User;
import com.bin.example.common.service.UserService;

/**
 * 提供具体的服务实现方法
 * @author zhangbin
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("user name = " + user.getName());
        return user;
    }
}
