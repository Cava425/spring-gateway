package com.simit.router.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simit.router.entity.GatewayFilter;
import com.simit.router.entity.GatewayPredicate;
import com.simit.router.entity.GatewayRoute;
import com.simit.router.service.RoutePublisherService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时地从 Dynamic Route Service 拉取路由信息
 * @Author: ys xu
 * @Date: 2020/6/12 22:19
 */
@Component
public class DynamicRouteScheduling {
    private RestTemplate rest;
    private RoutePublisherService routePublisherService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Long version = 0L;
    public DynamicRouteScheduling(@LoadBalanced RestTemplate rest, RoutePublisherService routePublisherService){
        this.rest = rest;
        this.routePublisherService = routePublisherService;
    }

    @Scheduled(cron = "*/60 * * * * ?")
    public void getDynamicRouteInfo() {
        try {
            System.out.println("拉取时间:" + LocalDateTime.now().format(formatter));
            //先拉取版本信息，如果版本号不想等则更新路由

            JSONObject lastVersion = rest.getForObject("http://dynamic-router-service" + "/version/latest", JSONObject.class);
            Long id = lastVersion.getLong("id");
            System.out.println("路由版本信息：本地版本号：" + version + "，远程版本号：" + id);
            if (id != null && version != id) {
                System.out.println("开始拉取路由信息......");
                String resultRoutes = rest.getForObject("http://dynamic-router-service" + "/gateway-routes/routes", String.class);
                System.out.println("路由信息为：" + resultRoutes);
                if (!StringUtils.isEmpty(resultRoutes)) {
                    List<GatewayRoute> gatewayRoutes = JSON.parseArray(resultRoutes, GatewayRoute.class);
                    for (GatewayRoute gatewayRoute : gatewayRoutes) {
                        //更新路由
                        RouteDefinition routeDefinition = assembleRoute(gatewayRoute);
                        routePublisherService.update(routeDefinition);
                    }
                    version = id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 把前端传递的参数转换成路由对象
    private RouteDefinition assembleRoute(GatewayRoute gatewayRoute) {
        RouteDefinition route = new RouteDefinition();
        route.setId(gatewayRoute.getId());
        route.setOrder(gatewayRoute.getOrder());

        // 设置断言
        List<PredicateDefinition> predicates = new ArrayList<>();
        List<GatewayPredicate> gatewayPredicates = gatewayRoute.getPredicates();
        for (GatewayPredicate p : gatewayPredicates) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(p.getArgs());
            predicate.setName(p.getName());
            predicates.add(predicate);
        }
        route.setPredicates(predicates);

        // 设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilter> gatewayFilters = gatewayRoute.getFilters();
        for (GatewayFilter f : gatewayFilters) {
            FilterDefinition filter = new FilterDefinition();
            filter.setName(f.getName());
            filter.setArgs(f.getArgs());
            filters.add(filter);
        }
        route.setFilters(filters);

        URI uri = null;
        if (gatewayRoute.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRoute.getUri()).build().toUri();
        } else {
            uri = URI.create(gatewayRoute.getUri());
        }
        route.setUri(uri);
        return route;
    }

}
