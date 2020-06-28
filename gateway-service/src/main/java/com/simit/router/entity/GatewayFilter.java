package com.simit.router.entity;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 网关路由过滤规则
 * @Author: ys xu
 * @Date: 2020/6/12 21:55
 */
public class GatewayFilter {
    private String name;
    private Map<String, String> args;

    public GatewayFilter(){
        args = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
