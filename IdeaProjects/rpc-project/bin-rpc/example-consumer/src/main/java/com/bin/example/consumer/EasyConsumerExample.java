package com.bin.example.consumer;

import com.bin.binrpc.proxy.ServiceProxyFactory;
import com.bin.example.common.model.User;
import com.bin.example.common.service.UserService;

public class EasyConsumerExample {
    public static void main(String[] args) {
        // 静态代理方式
        //UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        User result = userService.getUser(user);
        if (result != null) {
            System.out.println("result = " + result.getName());
        } else {
            System.out.println("result is null");
        }
    }
}
