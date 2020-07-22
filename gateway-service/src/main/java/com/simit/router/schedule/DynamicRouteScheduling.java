package com.simit.router.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simit.router.service.RoutePublisherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时地从 Dynamic Route Service 拉取路由信息
 * @Author: ys xu
 * @Date: 2020/6/12 22:19
 */
@Slf4j
@Component
public class DynamicRouteScheduling {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private RestTemplate rest;
    private RoutePublisherService routePublisherService;

    private Long version = 0L;

    public DynamicRouteScheduling(@LoadBalanced RestTemplate rest, RoutePublisherService routePublisherService){
        this.rest = rest;
        this.routePublisherService = routePublisherService;
    }

    @Scheduled(cron = "*/60 * * * * ?")
    public void task() {
        log.info("拉取时间：{}", LocalDateTime.now().format(formatter));

        try {
            JSONObject lastVersion = rest.getForObject("http://dynamic-router-service/version/latest", JSONObject.class);
            Long id = lastVersion.getLong("id");
            log.info("路由版本信息：本地版本号：{}，远程版本号：{}", version, id);

            if (id != null && version != id) {  // 如果有新的版本发布，则拉取新发布的路由
                JSONArray routes = rest.getForObject("http://dynamic-router-service/routes", JSONArray.class);
                log.info("拉取新发布的路由：{}", routes);

                if (routes.size() > 0) {    // 如果有路由信息，则进行更新
                    updateRouteDefinition(routes);
                    version = id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新路由信息
     * @param routes
     */
    private void updateRouteDefinition(JSONArray routes) {

        for (int i = 0; i < routes.size(); i++) {
            JSONObject route = routes.getJSONObject(i);
            String routeId = route.getString("id");
            String routeUri = route.getString("routeUri");
            String routeOrder = route.getString("routeOrder");
            JSONArray predicates = route.getJSONArray("predicates");
            JSONArray filters = route.getJSONArray("filters");


            List<PredicateDefinition> pds = new ArrayList<>();
            if(predicates != null){
                for (int j = 0; j < predicates.size(); j++) {
                    JSONObject predicate = predicates.getJSONObject(j);

                    PredicateDefinition pd = new PredicateDefinition();
                    String name = predicate.getString("name");
                    Map<String, String> args = toMap(predicate.getJSONObject("args"));
                    if(!Strings.isEmpty(name) || args.size() > 0){
                        pd.setName(name);
                        pd.setArgs(args);
                        pds.add(pd);
                    }
                }
            }

            List<FilterDefinition> fds = new ArrayList<>();
            if(filters != null){
                for (int j = 0; j < filters.size(); j++) {
                    JSONObject filter = filters.getJSONObject(j);

                    FilterDefinition fd = new FilterDefinition();
                    String name = filter.getString("name");
                    Map<String, String> args = toMap(filter.getJSONObject("args"));
                    if(!Strings.isEmpty(name) || args.size() > 0){
                        fd.setName(name);
                        fd.setArgs(args);
                        fds.add(fd);
                    }
                }
            }
            
            RouteDefinition definition = new RouteDefinition();
            definition.setId(routeId);
            definition.setOrder(Integer.parseInt(routeOrder));
            definition.setUri(URI.create(routeUri));
            definition.setPredicates(pds);
            definition.setFilters(fds);

            routePublisherService.update(definition);
            log.info("已经更新路由：{}", definition);
        }
    }

    /**
     * 将JSONObject对象转换为Map对象
     * @param object
     * @return
     */
    private Map toMap(JSONObject object){
        Map<String, String> ret = new LinkedHashMap<>();

        if(object == null){
            return ret;
        }

        for(String key : object.keySet()){
            ret.put(key, object.getString(key));
        }
        return ret;
    }

}
