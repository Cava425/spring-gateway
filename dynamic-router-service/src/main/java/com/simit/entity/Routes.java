package com.simit.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:30
 */
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


    /**
     * 获取断言集合
     * @return
     */
    public List<GatewayPredicate> getPredicateDefinition(){
        if(!StringUtils.isEmpty(predicates)){
            return JSON.parseArray(predicates , GatewayPredicate.class);
        }
        return null;
    }

    /**
     * 获取过滤器集合
     * @return
     */
    public List<GatewayFilter> getFilterDefinition(){
        if(!StringUtils.isEmpty(filters)){
            return JSON.parseArray(filters , GatewayFilter.class);
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    public Integer getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(Integer routeOrder) {
        this.routeOrder = routeOrder;
    }

    public Boolean getEbl() {
        return isEbl;
    }

    public void setEbl(Boolean ebl) {
        isEbl = ebl;
    }

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPredicates() {
        return predicates;
    }

    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "GatewayRoutes{" +
                "id=" + id +
                ", routeId='" + routeId + '\'' +
                ", routeUri='" + routeUri + '\'' +
                ", routeOrder=" + routeOrder +
                ", isEbl=" + isEbl +
                ", isDel=" + isDel +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", predicates='" + predicates + '\'' +
                ", filters='" + filters + '\'' +
                '}';
    }
}
