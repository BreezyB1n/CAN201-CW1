package com.bin.example.common.model;

import java.io.Serializable;

/**
 * 公共模块数据模型
 * @author zhangbin
 */
// 实现序列化 为了传输
public class User implements Serializable {

    private static final long serialVersionUID = -7523951106538014855L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
