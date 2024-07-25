package com.ly.webdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ly.webdemo.mapper")
public class WebdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebdemoApplication.class, args);
    }

}
