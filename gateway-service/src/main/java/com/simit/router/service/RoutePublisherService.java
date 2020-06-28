package com.simit.router.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 将路由事件注册到 Event Loop
 * @Author: ys xu
 * @Date: 2020/6/12 22:03
 */
@Service
public class RoutePublisherService implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter writer;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     * @param
     * @return
     * @date: 2020/6/12 22:15
     */
    public String add(RouteDefinition route) {
        writer.save(Mono.just(route)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * 更新路由
     * @param
     * @return
     * @date: 2020/6/12 22:16
     */
    public String update(RouteDefinition route) {
        try {
            delete(route.getId());
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + route.getId();
        }
        try {
            writer.save(Mono.just(route)).subscribe();
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    /**
     * 删除路由
     * @param
     * @return
     * @date: 2020/6/12 22:16
     */
    public Mono<ResponseEntity<Object>> delete(String id) {
        return writer.delete(Mono.just(id)).then(Mono.defer(() -> {
            return Mono.just(ResponseEntity.ok().build());
        })).onErrorResume(t -> {
            return t instanceof NotFoundException;
        }, t -> {
            return Mono.just(ResponseEntity.notFound().build());
        });
    }
}
