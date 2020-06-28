package com.simit.service;

import com.alibaba.fastjson.JSON;
import com.simit.config.RedisConfig;
import com.simit.entity.Version;
import com.simit.mapper.VersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:46
 */
@Service
public class VersionServiceImpl implements VersionService {
    @Resource
    private VersionMapper mapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RouteService routesService;

    @Override
    public int add(Version version) {
        version.setCreateTime(new Date());
        int result = mapper.insertSelective(version);

        //发布时，把版本信息与路由信息存入redis
        redisTemplate.opsForValue().set(RedisConfig.versionKey , String.valueOf(version.getId()));
        redisTemplate.opsForValue().set(RedisConfig.routeKey , JSON.toJSONString(routesService.getRouteDefinitions()));

        return result;
    }

    @Override
    public int update(Version version) {
        return mapper.updateByPrimaryKeySelective(version);
    }

    @Override
    public int delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取最后一次发布的版本号
     * @return
     */
    @Override
    public Long getLastVersion() {
        return mapper.getLastVersion();
    }

    @Override
    public List<Version> listAll() {
        return mapper.listAll();
    }
}
