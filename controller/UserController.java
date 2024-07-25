package com.ly.webdemo.controller;

import com.ly.webdemo.model.UserInfo;
import com.ly.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
/**
 * 而Spring Boot框架项目接口返回 JSON格式的数据比较简单：
 * 在 Controller 类中使用@RestController注解即可返回 JSON格式的数据。
 */
@RestController
@RequestMapping("/user")
public class UserController {
    // 属性注入service服务层的userService类
    @Autowired
    public UserService userService;
    @RequestMapping("/reg")
    public HashMap<String, Object> reg(String username, String password1, String password2) {
        HashMap<String, Object> result = new HashMap<>();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password1) || !StringUtils.hasLength(password2)) {
            result.put("status", -1);
            result.put("msg", "参数输入错误");
            result.put("data", "");
            return result;
        }
        else {
            if (!password1.equals(password2)) {
                result.put("status", -1);
                result.put("msg", "前后密码不一致");
                result.put("data", "");
                return result;
            }
            else {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(username);
                userInfo.setPassword(password1);
                int ret = userService.reg(userInfo);
                if (ret != 1) {
                    result.put("status", -1);
                    result.put("msg", "数据库添加出错");
                    result.put("data", "");
                    return result;
                }
                else {
                    result.put("status", 200);
                    result.put("msg", "注册成功");
                    result.put("data", ret);
                    return result;
                }
            }
        }
    }
    @RequestMapping("/login")
    public Object login(String username, String password) {
        HashMap<String, Object> result = new HashMap<>();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            result.put("status", -1);
            result.put("msg", "参数输入错误，登录失败");
            result.put("data", "");
            return result;
        }
        else {
            UserInfo userInfo = userService.selectByUsername(username);
            if (userInfo == null || !password.equals(userInfo.getPassword())) {
                result.put("status", -1);
                result.put("msg", "您当前的用户名或密码错误，请稍后再试");
                result.put("data", "");
                return result;
            }
            else {
                result.put("status", 200);
                result.put("msg", "恭喜，登录成功！");
                result.put("data", "");
                return result;
            }
        }
    }
}

