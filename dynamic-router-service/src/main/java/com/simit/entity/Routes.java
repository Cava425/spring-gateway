package com.simit.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:30
 */
@Data
@AllArgsConstructor
public class Routes {
    private Long id;
    private String routeId;
    private String routeUri;
    private Integer routeOrder;
    private Boolean isEbl;
    private Boolean isDel;
    private Date createTime;
    private Date updateTime;
    private String predicates;
    private String filters;
}
