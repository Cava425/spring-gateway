package com.simit.controller;

import com.simit.config.RedisConfig;
import com.simit.entity.Version;
import com.simit.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:23
 */
@Api(value = "版本接口")
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    private VersionService versionService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value="版本添加", notes="版本添加")
    @GetMapping("/add")
    public ResponseEntity<Version> add(){
        Version version = new Version();
        versionService.add(version);
        return new ResponseEntity<>(version, HttpStatus.CREATED);
    }

    // 获取最后一次发布的版本号
    @ApiOperation(value="最新版本", notes="最新版本")
    @GetMapping("/latest")
    public ResponseEntity<Version> getLastVersion(){
        Version version = new Version();
        String result = redisTemplate.opsForValue().get(RedisConfig.versionKey);
        if(!StringUtils.isEmpty(result)){
            System.out.println("返回 redis 中的版本信息......");
            version.setId(Long.parseLong(result));
        }else{
            System.out.println("返回 mysql 中的版本信息......");
            Long id = versionService.getLastVersion();
            version.setId(id);
            redisTemplate.opsForValue().set(RedisConfig.versionKey , String.valueOf(id));
        }
        if(version.getId() != 0){
            return new ResponseEntity<>(version, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    // 打开发布版本列表页面
    @ApiOperation(value = "版本列表", notes = "版本列表")
    @GetMapping("/versions")
    public ResponseEntity<List<Version>> listAll(){
        List<Version> versions = versionService.listAll();
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }
}
