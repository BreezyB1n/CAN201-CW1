package com.bin.binrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.bin.binrpc.model.RpcRequest;
import com.bin.binrpc.model.RpcResponse;
import com.bin.binrpc.serializer.JdkSerializer;
import com.bin.binrpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 服务代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:9090")
                    .body(bodyBytes)
                    .execute()) {

                if (httpResponse.getStatus() != 200) {
                    throw new RuntimeException("Failed with HTTP error code : " + httpResponse.getStatus());
                }

                byte[] result = httpResponse.bodyBytes();

                // 检查服务器返回的内容
                String responseBody = new String(result, StandardCharsets.UTF_8);
                //System.out.println("Response Body: " + responseBody);

                if (responseBody.startsWith("<!DOCTYPE")) {
                    throw new RuntimeException("Received HTML instead of expected binary data.");
                }

                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }



}
