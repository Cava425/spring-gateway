package com.simit.router.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: ys xu
 * @Date: 2020/6/12 21:55
 */
public class GatewayPredicate {
    private String name;
    private Map<String, String> args;

    public GatewayPredicate(){
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
