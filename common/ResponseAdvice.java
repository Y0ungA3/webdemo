package com.ly.webdemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;

/**
 * 数据格式同样返回(这个是兜底的，即使controller层返回的是int或string类型的数据，也可以转换成json格式）
 * 注意：只能兜底int类的返回（可以应对操作成功和失败两种情况）
 * 如果是String或其他格式的返回（可以应对操作成功的情况，但无法应对操作失败的情况，要继续对body进行特判）
 *
 * 这里我们只是以防万一，我们尽量还是选择再controller层直接返回json对象，这样更信息具体，更有怎针对性）
 */
@ControllerAdvice // 统一功能处理需要加这个注解
@ResponseBody
// @ResponseBody的作用就是把返回的对象转换为json格式，并把json数据写入response的body中，前台收到response时就可以获取其body中的json数据了。
public class ResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // 返回true才会继续执行下面的代码
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof HashMap) {
            return body;  // 此时已经是hashmap格式了
        }
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("state", 200);
//        result.put("msg", "");
//        result.put("data", body);
        if (body instanceof Integer){
            int num = (int) body;
            if (num <= 0) {
                // 应对int类型错误返回(查询文章列表，新增和删除博客可能会用到）——》也可能用不到，如果新增或查询失败，我直接就在controller就返回了（通过调用AjaxResult)
                // 新增、删除或查询失败（非得在controller返回int值，再通过这里返回json对象的话，不灵活（出错信息显示的不具体
                // 所以说这里我们只是以防万一，我们还是选择再controller层直接返回json对象，这样更信息具体，更有怎针对性）
                return AjaxResult.fail(-1,"抱歉，本次操作失败，请稍后再试！"); // 这里无法区分是新增失败还是删除失败
                // 这里我们本来返回的是一个hashmap格式的对象，但加了@ResponseBody，把我们的java对象转成的了json格式
            }
        }

        if (body == null) { // 应对String类型错误返回
            // 这里我们本来返回的是一个hashmap格式的对象，但加了@ResponseBody，把我们的java对象转成的了json格式
            return AjaxResult.fail(-1,"抱歉，查询失败！"); // 这时对查询当前用户的特判
        }
        if (body instanceof String) { // 以String类型正确返回
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(body); // 用jackson中的工具类返回(没弄明白）——》直接返回String吧（不序列化了，详见https://zhuanlan.zhihu.com/p/196372502）
        }
        // 这里我们本来返回的是一个hashmap格式的对象，但加了@ResponseBody，把我们的java对象转成的了json格式
        return AjaxResult.success(body);
        // 前端是通过result中的state值来判断操作是否成功的，这个类用来处理操作成功的情况（为操作成功的情况兜底）
        // 但这可能存在问题，如果操作失败，并且在controller层没有调用AjaxResult中的fail方法（而是直接返回，通过这个类来返回统一的数据格式，就会出现问题——》在这个类我们都是按成功的处理的）
        // 解决方案，在该类中提前判断body(判断操作失败的情况）--->我们约定如果操作失败就返回负数（在controller层调用AjaxResult的情况）

    }
}
