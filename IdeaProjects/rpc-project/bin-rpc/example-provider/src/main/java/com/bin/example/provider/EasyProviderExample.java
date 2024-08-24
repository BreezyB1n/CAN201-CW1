package com.bin.example.provider;


import com.bin.binrpc.register.LocalRegistry;
import com.bin.binrpc.server.HttpServer;
import com.bin.binrpc.server.VertxHttpServer;
import com.bin.example.common.service.UserService;

/**
 * 简易服务提供者
 * @author zhangbin
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动web服务
        HttpServer server = new VertxHttpServer();
        server.doStart(9090);
    }
}