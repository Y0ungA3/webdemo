package com.ly.webdemo.service;

import com.ly.webdemo.mapper.UserMapper;
import com.ly.webdemo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    public int reg(UserInfo userInfo) {
        return userMapper.reg(userInfo);
    }

    public UserInfo selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
