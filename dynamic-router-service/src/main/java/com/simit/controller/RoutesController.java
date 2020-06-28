package com.simit.controller;

import com.alibaba.fastjson.JSON;
import com.simit.config.RedisConfig;
import com.simit.entity.Routes;
import com.simit.service.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 1:03
 */
@Api(value = "路由接口")
@RestController
@RequestMapping("/gateway-routes")
public class RoutesController {

    @Autowired
    private RouteService routesService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取所有动态路由信息
     *
     * @return
     */
    @ApiOperation(value = "获取路由信息", notes = "获取路由信息")
    @GetMapping("/routes")
    public ResponseEntity<String> getRouteDefinitions() {
        //先从redis中取，再从mysql中取
        String result = redisTemplate.opsForValue().get(RedisConfig.routeKey);
        if (!StringUtils.isEmpty(result)) {
            System.out.println("返回 redis 中的路由信息......");
        } else {
            System.out.println("返回 mysql 中的路由信息......");
            result = JSON.toJSONString(routesService.getRouteDefinitions());
            //再set到redis
            redisTemplate.opsForValue().set(RedisConfig.routeKey, result);
        }
        System.out.println("路由信息：" + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    //打开添加路由页面
    @ApiOperation(value = "路由添加页面", notes = "路由添加页面")
    @GetMapping("/add")
    public String add(ModelMap map) {
        map.addAttribute("route", new Routes());
        return "addRoute";
    }

    //添加路由信息
    @ApiOperation(value = "添加路由信息", notes = "添加路由信息")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestBody Routes route) {
        return routesService.add(route) > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap map, Long id) {
        map.addAttribute("route", routesService.getById(id));
        return "addRoute";
    }

    //添加路由信息
    @ApiOperation(value = "修改路由信息", notes = "修改路由信息")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(@RequestBody Routes route) {
        return routesService.update(route) > 0 ? "success" : "fail";
    }

    //打开路由列表
    @ApiOperation(value = "路由列表", notes = "路由列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap map) {
        Routes route = new Routes();
        route.setDel(false);
        route.setEbl(false);
        map.addAttribute("list", routesService.getRoutes(route));
        return "routelist";
    }

    @ApiOperation(value = "路由列表", notes = "路由列表")
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        routesService.delete(id, true);
    }
}
