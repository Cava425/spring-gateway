package com.simit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @Author: ys xu
 * @Date: 2020/6/13 0:43
 */
@Data
@AllArgsConstructor
public class Version {
    private Long id;
    private String name;
    private String comment;
    private Date createAt;
}
