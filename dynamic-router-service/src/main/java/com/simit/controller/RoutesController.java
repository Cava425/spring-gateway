package com.simit.controller;

import com.alibaba.fastjson.JSONObject;
import com.simit.data.RoutesRepository;
import com.simit.entity.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("routes")
public class RoutesController {

    private RoutesRepository routesRepo;

    @Autowired
    public RoutesController(RoutesRepository routesRepo){
        this.routesRepo = routesRepo;
    }

    @GetMapping
    public ResponseEntity<Iterable<Routes>> getAll(){
        Iterable<Routes> routes = routesRepo.findAll();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Routes> addRoutes(@RequestBody JSONObject object){

        Routes routes = new Routes(null,
                object.getString("route_id"),
                object.getString("route_uri"),
                object.getInteger("route_order"),
                false,
                false,
                null,
                null,
                object.getJSONArray("predicates").toString(),
                object.getJSONArray("filters").toString()
        );

        routes = routesRepo.save(routes);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }
}
