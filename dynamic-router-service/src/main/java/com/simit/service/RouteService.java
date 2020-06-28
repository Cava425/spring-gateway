package com.simit.service;

import com.simit.entity.GatewayRoute;
import com.simit.entity.Routes;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:29
 */
public interface RouteService {
    int add(Routes route);

    int update(Routes route);

    int delete(Long id, boolean isDel);

    int enableById(Long id, boolean isEbl);

    Routes getById(Long id);

    /**
     * 查询路由信息
     * @return
     */
    List<Routes> getRoutes(Routes route);

    /**
     * 返回组装后网关需要的路由信息
     * @return
     */
    List<GatewayRoute> getRouteDefinitions();
}
