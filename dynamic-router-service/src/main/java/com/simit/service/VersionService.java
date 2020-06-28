package com.simit.service;

import com.simit.entity.Version;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:45
 */
public interface VersionService {
    int add(Version version);

    int update(Version version);

    int delete(Long id);

    /**
     * 获取最后一次发布的版本号
     * @return
     */
    Long getLastVersion();

    //获取所有的版本发布信息
    List<Version> listAll();
}
