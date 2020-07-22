package com.simit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "t_user")
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private boolean available;

    private String phoneNumber;

    private String email;

    private String autoType;

    private String lastTime;

    private String smsCode;

    private String emailCode;

    private String openId;

    private String nickName;

    private String headImgUrl;

    @Transient
    @TableField(exist = false)
    private List<Role> roles;

    @Transient
    @TableField(exist = false)
    private List<Permission> permissions;

}
