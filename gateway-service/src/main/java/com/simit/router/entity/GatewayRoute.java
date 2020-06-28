package com.simit.router.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/12 21:55
 */
public class GatewayRoute {
    private String id;
    private List<GatewayPredicate> predicates;
    private List<GatewayFilter> filters;
    private String uri;
    private int order;

    public GatewayRoute(){
        predicates = new ArrayList<>();
        filters = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GatewayPredicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<GatewayPredicate> predicates) {
        this.predicates = predicates;
    }

    public List<GatewayFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<GatewayFilter> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
