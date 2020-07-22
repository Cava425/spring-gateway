package com.simit.controller;

import com.simit.data.VersionRepository;
import com.simit.entity.Version;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:23
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    private VersionRepository versionRepo;

    @Autowired
    public VersionController(VersionRepository versionRepo){
        this.versionRepo = versionRepo;
    }

    @GetMapping("/latest")
    public ResponseEntity<Version> getLatestVersion(){
        Version version = versionRepo.findLatest();

        return new ResponseEntity<>(version, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Version>> getAll(){
        Iterable<Version> versions = versionRepo.findAll();
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @PostMapping
    public Version addVersion(@RequestBody Version version){
        version = versionRepo.save(version);
        return version;
    }




}
