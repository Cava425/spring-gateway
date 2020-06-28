package com.simit;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id){
        return "Id: " + id + " has delete";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id){
        return "product: " + id;
    }
}
