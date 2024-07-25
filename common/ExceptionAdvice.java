package com.ly.webdemo.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 统一异常处理
 */
@ControllerAdvice
@ResponseBody
// @ResponseBody的作用就是把返回的对象转换为json格式，并把json数据写入response的body中，前台收到response时就可以获取其body中的json数据了。
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public Object exceptionAdvice(Exception e) {
        // 这里我们本来返回的是一个hashmap格式的对象，但加了@ResponseBody，把我们的java对象转成的了json格式
        return AjaxResult.fail(-1, e.getMessage());
    }
    // 你这里的统一异常处理，并不完整

}
