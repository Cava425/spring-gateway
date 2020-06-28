package com.simit.service;

import com.simit.entity.GatewayRoute;
import com.simit.entity.Routes;
import com.simit.mapper.RoutesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:40
 */
@Service
public class RouteServiceImpl implements RouteService {
    @Resource
    private RoutesMapper mapper;
    @Override
    public int add(Routes route) {
        route.setEbl(false);
        route.setDel(false);
        route.setCreateTime(new Date());
        route.setUpdateTime(new Date());
        return mapper.insertSelective(route);
    }

    @Override
    public int update(Routes route) {
        route.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(route);
    }

    @Override
    public int delete(Long id, boolean isDel) {
        return mapper.deleteByPrimaryKey(id , isDel);
    }

    @Override
    public int enableById(Long id, boolean isEbl) {
        return mapper.enableById(id , isEbl);
    }

    @Override
    public Routes getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询路由信息
     * @return
     */
    @Override
    public List<Routes> getRoutes(Routes route) {
        return mapper.getRoutes(route);
    }

    /**
     * 返回组装后网关需要的路由信息
     * @return
     */
    @Override
    public List<GatewayRoute> getRouteDefinitions() {
        List<GatewayRoute> routeDefinitions = new ArrayList<>();
        Routes route = new Routes();
        route.setDel(false);
        route.setEbl(false);
        List<Routes> routes = getRoutes(route);
        for(Routes r : routes){
            GatewayRoute routeDefinition = new GatewayRoute();
            routeDefinition.setId(r.getRouteId());
            routeDefinition.setUri(r.getRouteUri());
            routeDefinition.setFilters(r.getFilterDefinition());
            routeDefinition.setPredicates(r.getPredicateDefinition());
            //一开始忘记这个了
            routeDefinition.setOrder(r.getRouteOrder());
            routeDefinitions.add(routeDefinition);
        }
        return routeDefinitions;
    }
}
