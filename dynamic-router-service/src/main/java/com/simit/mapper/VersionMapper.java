package com.simit.mapper;

import com.simit.entity.Version;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:52
 */
public interface VersionMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Version record);
    int insertSelective(Version record);
    Version selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(Version record);
    int updateByPrimaryKey(Version record);
    //获取最后一次发布的版本号
    Long getLastVersion();
    //获取所有的版本发布信息
    List<Version> listAll();
}
