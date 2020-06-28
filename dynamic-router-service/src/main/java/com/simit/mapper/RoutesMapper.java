package com.simit.mapper;

import com.simit.entity.Routes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:55
 */
public interface RoutesMapper {
    int deleteByPrimaryKey(@Param("id") Long id, @Param("isDel") boolean isDel);

    int enableById(@Param("id") Long id, @Param("isEbl") boolean isEbl);

    int insert(Routes record);

    int insertSelective(Routes record);

    Routes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Routes record);

    int updateByPrimaryKey(Routes record);

    List<Routes> getRoutes(Routes route);
}
