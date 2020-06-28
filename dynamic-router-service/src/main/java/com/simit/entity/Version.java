package com.simit.entity;

import java.util.Date;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:43
 */
public class Version {
    private Long id;
    private Date createTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
