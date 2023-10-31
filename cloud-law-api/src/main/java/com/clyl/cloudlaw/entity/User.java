package com.clyl.cloudlaw.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String userId;
    private String userName;
    private String openId;
    private String password;
    private String phone;
    private String avatar;
    private String createTime;
    private String updateTime;
}
