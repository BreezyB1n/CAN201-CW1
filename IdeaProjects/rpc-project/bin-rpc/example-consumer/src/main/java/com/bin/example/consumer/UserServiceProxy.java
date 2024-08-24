package com.bin.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.bin.binrpc.model.RpcRequest;
import com.bin.binrpc.model.RpcResponse;
import com.bin.binrpc.serializer.JdkSerializer;
import com.bin.binrpc.serializer.Serializer;
import com.bin.example.common.model.User;
import com.bin.example.common.service.UserService;

/**
 * UserService静态代理
 * @author zhangbin
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .args(new Object[]{user})
                .parameterTypes(new Class[]{User.class})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;

            try(HttpResponse response = HttpRequest.post("http://localhost:9090/getUser")
                    .body(bodyBytes)
                    .execute()) {

                int statusCode = response.getStatus();
                if (statusCode != 200) {
                    throw new RuntimeException("Failed with HTTP error code : " + statusCode);
                }

                result = response.bodyBytes();
            }

            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (Exception e) {
            System.err.println("Error occurred while invoking getUser:");
            e.printStackTrace();
        }

        return null;
    }

}
