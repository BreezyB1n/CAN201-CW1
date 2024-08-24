package com.bin.binrpc.server;

/**
 * HTTP服务接口
 * @author zhangbin
 */
public interface HttpServer {

    /**
     * 启动HTTP服务
     * @param port 端口
     */
    void doStart(int port);
}
